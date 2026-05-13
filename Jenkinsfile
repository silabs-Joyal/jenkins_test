pipeline {
    agent {
        label 'linux'
    }

    stages {
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
