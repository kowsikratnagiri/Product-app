pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'  // Configure in Jenkins Global Tools
        jdk 'JAVA_HOME'
    }

    environment {
        MVN_OPTS = '-DskipTests'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/kowsikratnagiri/Product-app.git'
            }
        }

        stage('Run Config Server') {
            steps {
                dir('config-server') {
                    sh "nohup mvn spring-boot:run &"
                    sleep(time:20, unit:"SECONDS") // give it time to start
                }
            }
        }

        stage('Build Discovery Service') {
            steps {
                dir('discovery-server') {
                    sh "mvn clean install ${env.MVN_OPTS}"
                }
            }
        }

        stage('Build Gateway Service') {
            steps {
                dir('gateway-service') {
                    sh "mvn clean install ${env.MVN_OPTS}"
                }
            }
        }

        stage('Build Auth Service') {
            steps {
                dir('auth-server') {
                    sh "mvn clean install ${env.MVN_OPTS}"
                }
            }
        }
    }
}
