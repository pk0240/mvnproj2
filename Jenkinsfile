pipeline {
    agent any

    tools {
        jdk 'java17'
        maven 'Maven'
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "Pulling from GITHUB repository"
                git branch: 'main',
                    credentialsId: 'mygithubcred',
                    url: 'https://github.com/pk0240/mvnproj2.git'
            }
        }

        stage('Test the Project') {
            steps {
                echo "Test my JAVA project"
                bat 'mvn clean test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    echo 'Test Run succeeded!'
                }
            }
        }

        stage('Build Project') {
            steps {
                echo "Building my JAVA project"
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build the Docker Image') {
            steps {
                echo "Build the Docker Image for mvn project"
                bat 'docker build -t mvnproj:1.0 .'
            }
        }

         stage('Push Docker Image to DockerHub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhubpwd',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    bat '''
                    docker logout
                    echo %DOCKER_PASS%| docker login -u %DOCKER_USER% --password-stdin
                    docker tag mvnproj:1.0 %DOCKER_USER%/myapp:latest
                    docker push %DOCKER_USER%/myapp:latest
                    '''
                }
            }
        }

        stage('Deploy Container') {
            steps {
                echo "Running Docker container"
                bat '''
                docker rm -f myjavaapp || exit 0
                docker run -d --name myjavaapp -p 8080:8080 priyanrk17/myapp:latest
                '''
            }
        }
    }


    post {
        success {
            echo 'I succeeded!'
        }
        failure {
            echo 'Failed........'
        }
    }
}
