
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Validate') {
            steps {
                script {
                    if (!fileExists('index.html')) {
                        error 'index.html not found!'
                    }
                }
                echo 'Application validation successful.'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    mkdir -p "$WORKSPACE/deployment"
                    cp index.html "$WORKSPACE/deployment/index.html"
                '''
                echo 'Deployment completed successfully.'
            }
        }

        stage('Verify') {
            steps {
                sh '''
                    test -s "$WORKSPACE/deployment/index.html"
                    grep -q "Continuous Deployment with Jenkins" \
                        "$WORKSPACE/deployment/index.html"
                '''
                echo 'Deployment verification successful.'
            }
        }
    }

    post {
        success {
            echo 'CD pipeline completed successfully.'
        }
        failure {
            echo 'CD pipeline failed. Check the console output.'
        }
    }
}
