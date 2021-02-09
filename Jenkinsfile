pipeline {
    agent any

    tools {
        maven "M3"
    }

    stages {
        stage('Build') {
            steps{
                dir('business-allocation-app'){
                    sh "mvn clean"
                    sh "mvn compile"
                    sh "mvn test"
                    sh "mvn package"
                }
            }

            }
        }
    }
