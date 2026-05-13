pipeline {
    agent any

    stages {

        stage('Get Folder Path') {
            steps {
                script {
                    def fullJobName = env.JOB_NAME
                    def parts = fullJobName.tokenize('/')
                    // Top-level jobs have no folder; [0..-2] on a single element throws IndexOutOfBoundsException.
                    def folderPath = parts.size() > 1 ? parts[0..-2].join('/') : ''

                    echo "Folder path: ${folderPath}"
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
