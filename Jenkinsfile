pipeline {
    agent any

    stages {

        stage('Get Folder Path') {
            steps {
                script {
                    def parts = env.JOB_NAME.tokenize('/')
                    def n = parts.size()
                    // n < 2: no stack segment; n < 1: empty JOB_NAME
                    def testType = n ? parts[-1] : ''
                    def stackName = n > 1 ? parts[-2] : ''
                    def folderPath = n > 1 ? parts.init().join('/') : ''

                    echo """JOB_NAME: ${env.JOB_NAME}
                            stackName: ${stackName}
                            testType: ${testType}
                            folderPath: ${folderPath}
                         """
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
