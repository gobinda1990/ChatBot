pipeline {
    agent any 
  tools {
    maven 'maven'  // Use the name you gave in Global Tool Config
         
  }
    environment {
        GITHUB_REPO = 'https://github.com/gobinda1990/ChatBot.git'  
        SONAR_HOST_URL = 'http://10.153.43.8:9000'
        SONARQUBE_SERVER = 'MySonarQube'
    }

    stages {

        stage('Clone code from GitHub') {
            steps {
               git branch: 'main', url: "${env.GITHUB_REPO}"                
            }
        }
        stage('Setup Oracle Driver') {
            steps {
                sh '''
                  mvn install:install-file \
                    -Dfile=libs/ojdbc6.jar \
                    -DgroupId=com.oracle \
                    -DartifactId=ojdbc6 \
                    -Dversion=11.2.0.4 \
                    -Dpackaging=jar
                '''
            }
            }
        stage('Build') {
      steps {
        sh 'mvn clean install -U'
      }
    }
        stage('Compile') {
            steps {
                sh "mvn compile"
            }
        }
        stage('Test') {
            steps {
                sh "mvn test"
            }
        }

        stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv("${SONARQUBE_SERVER}") {
            sh '''
                mvn sonar:sonar \
                  -Dsonar.projectKey=ChatBotKey \
                  -Dsonar.projectName=ChatBot \
                  -Dsonar.java.binaries=target \
                  -Dsonar.host.url=$SONAR_HOST_URL \
                  -Dsonar.login=$SONAR_AUTH_TOKEN
            '''
        }
    }
}        

        // stage('Package') {
        //     steps {
        //         sh 'mvn package -DskipTests'
        //     }
        // }        
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            echo 'Build and deployment succeeded.'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
