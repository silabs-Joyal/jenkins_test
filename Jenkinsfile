pipeline {
    agent any

    stages {

        stage('Get Folder Path') {
            steps {
                script {
                    def fullJobName = env.JOB_NAME
                    def folders = fullJobName.tokenize('/')[0..-2]  // remove job name
                    def folderPath = folders.join('/')

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
