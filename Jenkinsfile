pipeline {
    agent any

    tools {
        jdk 'java17'
        maven 'Maven'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    credentialsId: 'mygithubcred',
                    url: 'https://github.com/pk0240/mvnproj2.git'
            }
        }

        stage('Test Project') {
            steps {
                bat 'mvn clean test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Project') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t mymvnproj:latest .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhubpwd',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat '''
                    echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                    docker tag mymvnproj:latest %DOCKER_USER%/mymvnproj:latest
                    docker push %DOCKER_USER%/mymvnproj:latest
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat '''
                "C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" start
                kubectl apply -f deployment.yaml
                timeout /t 20
                kubectl get pods
                kubectl apply -f services.yaml
                kubectl get svc
                '''
            }
        }

        stage('Parallel Operations') {
            parallel {

                stage('Enable Metrics') {
                    steps {
                        bat '"C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" addons enable metrics-server'
                    }
                }

                stage('Verify Services') {
                    steps {
                        bat 'kubectl get svc'
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully 🚀'
        }
        failure {
            echo 'Pipeline failed ❌'
        }
    }
}
