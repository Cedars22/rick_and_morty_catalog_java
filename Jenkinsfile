pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './mvnw -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw -B test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeLocal') {
                    sh './mvnw -B sonar:sonar -Dsonar.projectKey=rick-and-morty-catalog -Dsonar.projectName=rick-and-morty-catalog'
                }
            }
        }
    }
}
