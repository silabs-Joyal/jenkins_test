pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'echo "Replace this with your build (e.g. npm ci && npm run build, mvn clean package)."'
                    } else {
                        bat 'echo Replace this with your build command.'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'echo "Replace this with your tests (e.g. npm test, mvn test)."'
                    } else {
                        bat 'echo Replace this with your test command.'
                    }
                }
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
