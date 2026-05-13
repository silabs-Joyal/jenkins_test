// tests/<stack>/<jobType>/testConfig.yaml -> Job DSL (folder + pipelineJob). Needs jobDsl + findFiles.

node {
    def gitUrl = env.JENKINS_TEST_GIT_URL ?: 'https://github.com/silabs-Joyal/jenkins_test.git'
    def top = (env.MANAGER_JOBS_ROOT ?: '').trim()
    def creds = (env.MANAGER_GIT_CREDENTIALS_ID ?: '').trim()
    def scriptPath = (env.MANAGER_SCRIPT_PATH ?: 'jenkins/tester.groovy').trim()
    def branch = (env.MANAGER_GIT_BRANCH ?: '*/main').trim()
    def q = { s -> s.replace('\\', '\\\\').replace("'", "\\'") }

    checkout scm
    def files = findFiles(glob: 'tests/*/*/testConfig.yaml')
    if (!files) error('No tests/*/*/testConfig.yaml in workspace.')

    def byStack = [:].withDefault { [] as Set }
    files.each {
        def p = it.path.replace('\\', '/').split('/')
        if (p.size() >= 4 && p[0] == 'tests') byStack[p[1]] << p[2]
    }

    def dsl = new StringBuilder()
    def emit = { stack, jobType ->
        dsl << "pipelineJob('${q(jobType)}') {\n"
        dsl << "  description('${q("tests/${stack}/${jobType}")}')\n"
        dsl << "  definition {\n    cpsScm {\n      scm {\n        git {\n"
        dsl << "          remote {\n            url('${q(gitUrl)}')\n"
        if (creds) dsl << "            credentials('${q(creds)}')\n"
        dsl << "          }\n          branch('${q(branch)}')\n        }\n      }\n"
        dsl << "      scriptPath('${q(scriptPath)}')\n      lightweight(false)\n    }\n  }\n}\n"
    }

    if (top) {
        dsl << "folder('${q(top)}') {\n"
        byStack.each { stack, jobs ->
            dsl << "  folder('${q(stack)}') {\n"
            jobs.each { emit(stack, it) }
            dsl << "  }\n"
        }
        dsl << "}\n"
    } else {
        byStack.each { stack, jobs ->
            dsl << "folder('${q(stack)}') {\n"
            jobs.each { emit(stack, it) }
            dsl << "}\n"
        }
    }

    def out = 'jenkins/.manager-generated-dsl.groovy'
    writeFile file: out, text: dsl.toString()
    jobDsl targets: out
}
