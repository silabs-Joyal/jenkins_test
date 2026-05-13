/**
 * Job DSL seed: mirrors tests/<stack>/<jobType>/ as Folder(stack) + Pipeline job(jobType).
 *
 * Run from a seed job that checks out this repo so WORKSPACE contains ./tests.
 * Requires the Job DSL plugin. Optional env vars:
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

def workspaceRoot = new File(System.getenv('WORKSPACE') ?: '.')
def testsRoot = new File(workspaceRoot, 'tests')
if (!testsRoot.isDirectory()) {
    throw new IllegalStateException("tests/ not found under WORKSPACE: ${testsRoot.absolutePath}")
}

if (jobsRoot) {
    folder(jobsRoot) {
        testsRoot.eachDir { stackDir ->
            def stack = stackDir.name
            folder(stack) {
                stackDir.eachDir { typeDir ->
                    def jobType = typeDir.name
                    pipelineJob(jobType) {
                        description("Managed from tests/${stack}/${jobType}")
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
    testsRoot.eachDir { stackDir ->
        def stack = stackDir.name
        folder(stack) {
            stackDir.eachDir { typeDir ->
                def jobType = typeDir.name
                pipelineJob(jobType) {
                    description("Managed from tests/${stack}/${jobType}")
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
