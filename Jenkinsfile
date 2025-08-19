pipeline {
    agent any 
  tools {
    maven 'maven'  // Use the name you gave in Global Tool Config
  }
    environment {
        GITHUB_REPO = 'https://github.com/gobinda1990/ChatBot.git'        
        SCANNER_HOME = tool 'sonar'
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
                    -Dfile=lib/ojdbc6-11.2.0.4.jar \
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

        stage('SonarQube Quality Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh "$SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=ChatBot -Dsonar.projectKey=ChatBotKey -Dsonar.java.binaries=target"
                }
            }
        }       

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }        
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
