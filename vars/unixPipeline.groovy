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
                    withSonarQubeEnv('POC-Sonar') {
					script {
                        def scannerHome = tool name: 'SonarScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                        sh "${scannerHome}/bin/sonar-scanner \
                              -Dsonar.projectKey=${config.projectKey} \
                              -Dsonar.sources=. \
                              -Dsonar.host.url=http://sonarqube:9000 \
                              -Dsonar.login='sonar-token'"
						}
                    }
                }
            }
            stage('Generate Version in DA') {
                steps {
                    echo "Versión Generada"
                }
            }
        }
    }
}
