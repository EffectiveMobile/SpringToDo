pipeline {
    agent any

    environment {
        IMAGE_NAME = 'ohrimpavel/spring-todo-app'
        DOCKER_CREDENTIALS_ID = 'dockerhub-creds'
        MAVEN_HOME = tool name: 'Maven 3', type: 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean install"
            }
        }

        stage('Test') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn test"
            }
        }

        stage('Docker Build & Push') {
            when {
                branch 'main'
            }
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        def image = docker.build("${IMAGE_NAME}")
                        image.push("latest")
                    }
                }
            }
        }
    }
}
