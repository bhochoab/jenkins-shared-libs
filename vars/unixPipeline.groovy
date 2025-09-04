def call(Map config = [:]) {
    pipeline {
        agent any
        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }
            stage('SonarQube Analysis') {
                steps {
                    withSonarQubeEnv('SonarQubeServer') {
                        sh """
                            sonar-scanner \
                              -Dsonar.projectKey=${config.projectKey} \
                              -Dsonar.sources=. \
                              -Dsonar.host.url=http://sonarqube:9000 \
                              -Dsonar.login='sonar-token'
                        """
                    }
                }
            }
            stage('Generate Version in DA') {
                steps {
                    sh """
                        da-cli create-version \
                          --app ${config.appName} \
                          --component ${config.component} \
                          --version ${env.BUILD_NUMBER}
                    """
                }
            }
        }
    }
}
