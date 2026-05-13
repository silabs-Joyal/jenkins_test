/**
 * Job DSL seed: mirrors tests/<stack>/<jobType>/ as Folder(stack) + Pipeline job(jobType).
 *
 * Run from a seed job that checks out this repo so WORKSPACE contains ./tests.
 * Layout is discovered via tests//testConfig.yaml (sandbox-safe; avoids java.io.File).
 * Requires Job DSL, pipeline-utility-steps (findFiles), and SCM on the job so checkout scm works.
 *   JENKINS_TEST_GIT_URL          Git remote (override in seed job or controller env)
 *   MANAGER_JOBS_ROOT             Optional top-level folder (e.g. silabs_jobs)
 *   MANAGER_GIT_CREDENTIALS_ID    Optional Jenkins credentials id for the Git remote
 *   MANAGER_SCRIPT_PATH           Pipeline script in repo (default: jenkins/tester.groovy)
 *   MANAGER_GIT_BRANCH            Branch spec (default: *
 *                                   /main)
 */

def gitUrl = System.getenv('JENKINS_TEST_GIT_URL') ?: 'https://github.com/silabs-Joyal/jenkins_test.git'
def jobsRoot = (System.getenv('MANAGER_JOBS_ROOT') ?: '').trim()
def credsId = (System.getenv('MANAGER_GIT_CREDENTIALS_ID') ?: '').trim()
def scriptPath = (System.getenv('MANAGER_SCRIPT_PATH') ?: 'jenkins/tester.groovy').trim()
def branchSpec = (System.getenv('MANAGER_GIT_BRANCH') ?: '*/main').trim()

node {
    checkout scm

    def configs = findFiles(glob: 'tests/*/*/testConfig.yaml')
    if (!configs) {
        throw new IllegalStateException(
            'No tests/<stack>/<jobType>/testConfig.yaml under WORKSPACE. Checkout this repo so ./tests exists.'
        )
    }

    def jobSpecs = []
    def seen = [] as Set
    configs.each { f ->
        def norm = f.path.replace('\\', '/')
        def parts = norm.split('/')
        if (parts.size() < 4 || parts[0] != 'tests') {
            return
        }
        def stack = parts[1]
        def jobType = parts[2]
        def key = "${stack}/${jobType}"
        if (seen.add(key)) {
            jobSpecs << [stack: stack, jobType: jobType]
        }
    }

    def byStack = jobSpecs.groupBy { it.stack }

    if (jobsRoot) {
        folder(jobsRoot) {
            byStack.each { stack, items ->
                folder(stack) {
                    items.each { spec ->
                        pipelineJob(spec.jobType) {
                            description("Managed from tests/${stack}/${spec.jobType}")
                            definition {
                                cpsScm {
                                    scm {
                                        git {
                                            remote {
                                                url(gitUrl)
                                                if (credsId) {
                                                    credentials(credsId)
                                                }
                                            }
                                            branch(branchSpec)
                                        }
                                    }
                                    scriptPath(scriptPath)
                                    lightweight(false)
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        byStack.each { stack, items ->
            folder(stack) {
                items.each { spec ->
                    pipelineJob(spec.jobType) {
                        description("Managed from tests/${stack}/${spec.jobType}")
                        definition {
                            cpsScm {
                                scm {
                                    git {
                                        remote {
                                            url(gitUrl)
                                            if (credsId) {
                                                credentials(credsId)
                                            }
                                        }
                                        branch(branchSpec)
                                    }
                                }
                                scriptPath(scriptPath)
                                lightweight(false)
                            }
                        }
                    }
                }
            }
        }
    }
}
