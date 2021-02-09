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
            }


            }
        }

    stages {
        stage('Test') {
        dir('business-allocation-app'){
                        steps {
                                        sh "mvn test"
                                        }
                    }
           

            }
        }
    }
}
