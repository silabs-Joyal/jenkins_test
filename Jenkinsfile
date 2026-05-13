pipeline {
    agent any

    stages {

        stage('Initialize') {
            steps {
                script {
                    def segs = env.JOB_NAME.tokenize('/')
                    def jobType = segs ? segs.last() : ''

                    def config = readYaml file: 'triggers.yaml'
                    def cronString = config.triggers[jobType] ?: ''
                    if (cronString?.trim()) {
                        properties([
                            pipelineTriggers([
                                cron(cronString.trim())
                            ])
                        ])
                    }

                    def n = segs.size()
                    def stackName = n > 1 ? segs[-2] : ''
                    echo "JOB_NAME: ${env.JOB_NAME}"
                    echo "stackName: ${stackName}"
                    echo "jobType: ${jobType}"
                    echo "scheduledCron: ${cronString ?: '(none)'}"
                }
            }
        }

        stage('Build') {
            steps {
                sh 'echo "Replace this with your build (e.g. npm ci && npm run build, mvn clean package)."'
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Replace this with your tests (e.g. npm test, mvn test)."'
            }
        }
    }

    post {
        success {
            echo 'Pipeline finished successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
