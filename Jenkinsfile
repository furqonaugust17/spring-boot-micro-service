pipeline {
    agent any

    environment {
        REGISTRY = "furqonaugustseventeenth"
        IMAGE_NAME = "pustaka-${env.JOB_NAME}"
        K8S_DIR = "k8s/${env.JOB_NAME}"    // Folder YAML service
        GITHUB_CREDENTIAL = "github_pat"
        DOCKER_CREDENTIAL = "dockerhub_cred"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: "${GITHUB_CREDENTIAL}",
                    url: 'https://github.com/furqonaugust17/spring-boot-micro-service.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh """
                    echo "Running Maven Build..."
                    mvn -DskipTests clean package
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                        echo "Building Docker Image..."
                        docker build -t ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} .
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIAL}",
                                    usernameVariable: "USER",
                                    passwordVariable: "PASS")]) {

                        sh """
                            echo "Logging in to Docker Hub..."
                            echo "${PASS}" | docker login -u "${USER}" --password-stdin
                            docker push ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}
                        """
                    }
                }
            }
        }

        stage('Update Kubernetes Manifest') {
            steps {
                script {
                    sh """
                        echo "Updating Kubernetes YAML with new image tag..."
                        sed -i 's|image: ${REGISTRY}/${IMAGE_NAME}:.*|image: ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}|g' ${K8S_DIR}/deployment.yaml
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh """
                        echo "Applying Kubernetes Deployment..."
                        kubectl apply -f ${K8S_DIR}/
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Deploy Success!"
        }
        failure {
            echo "Deploy Failed!"
        }
    }
}
