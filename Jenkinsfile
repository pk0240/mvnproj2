pipeline {
    agent any
     tools{
         jdk 'java17'
         maven 'Maven'
    }
    stages {
        stage('Checkout Code') {
            steps {
               echo "Pulling from GITHUB repository"
               git branch: 'main', credentialsId: 'mygithubcred', url: 'https://github.com/pk0240/mvnproj2.git'
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
        stage(' Build the Docker Image') {
            steps {
               echo "Build the Docker Image for mvn project"
               bat 'docker build -t mvnproj2:1.0 .'
            }
        }
         stage('Push Docker Image to DockerHub') {
            steps {
               echo "Push Docker Image to DockerHub for mvn project"
                 withCredentials([string(credentialsId: 'priyanrk17', variable: 'DOCKER_PASS')]) {
                         bat '''
   	        echo %DOCKER_PASS% | docker login -u priyanrk17 --password-stdin
                         docker tag mvnproj2:1.0 priyanrk17/mymvnproj2:1.0:latest
                         docker push priyanrk17/mymvnproj2:1.0:latest
                         '''
                  }
            }
        }
       
        stage('Deploy the project using Container') {
            steps {
                echo "Running Java Application"
                bat '''
	docker rm -f myjavaappcont || exit 0
	docker run --name myjavaappcont priyanrk17/mymvnproj2:latest
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
