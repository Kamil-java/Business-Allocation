pipeline {
    agent any

    tools {
        maven "M3"
    }

    stages {
        stage('Build') {
            dir('business-allocation-app'){
                steps{
                    sh "mvn clean compile"
                    }
                    steps {
                                                             sh "mvn test"
                                                             }
            }


            }
        }


    }
}
