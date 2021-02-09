pipeline {
    agent any

    tools {
        maven "M3"
    }

    stages {
        stage('Build') {
            steps{
                dir('business-allocation-app'){
                    sh "mvn clean compile"
                     sh "mvn test"
                }
            }
           
            }
        }
    }
