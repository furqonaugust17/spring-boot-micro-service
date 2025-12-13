pipeline {
    agent any

    tools {
        maven 'Maven-3'
        jdk 'jdk-17'
    }

    environment {
        DOCKER_REGISTRY_USER = 'furqonaugustseventeenth' 
        
        DOCKER_CRED_ID = 'dockerhub_cred'
        
        IMAGE_TAG = "${BUILD_NUMBER}"
        
        K8S_MANIFEST_DIR = 'k8s' 
        
        SKIP_TESTS = '-DskipTests'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git...'
                checkout scm
            }
        }

        stage('Build Maven JARs') {
            steps {
                script {
                    echo 'Building JAR files...'
                    dir('anggota_service') { sh "mvn clean package ${SKIP_TESTS}" }
                    dir('buku_service') { sh "mvn clean package ${SKIP_TESTS}" }
                    dir('peminjaman_service') { sh "mvn clean package ${SKIP_TESTS}" }
                    dir('pengembalian_service') { sh "mvn clean package ${SKIP_TESTS}" }
                    dir('api-gateway-pustaka') { sh "mvn clean package ${SKIP_TESTS}" }
                }
            }
        }

        stage('Build & Push Docker Images') {
            steps {
                script {
                    docker.withRegistry('', "${DOCKER_CRED_ID}") {
                        parallel(
                            // --- 1. Infrastructure Images (Logstash & Filebeat Custom) ---
                            'Build Infra Images': {
                                echo 'Building Logstash & Filebeat...'
                                
                                // Build & Push Logstash
                                def logstashImg = docker.build("${DOCKER_REGISTRY_USER}/logstash:${IMAGE_TAG}", "-f logstash/Dockerfile .")
                                logstashImg.push()
                                logstashImg.push("latest")

                                // Build & Push Filebeat (digunakan oleh semua sidecar)
                                def filebeatImg = docker.build("${DOCKER_REGISTRY_USER}/filebeat:${IMAGE_TAG}", "-f filebeat/Dockerfile .")
                                filebeatImg.push()
                                filebeatImg.push("latest")
                            },

                            // --- 2. Microservices Images ---
                            'Build Anggota': {
                                dir('anggota_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/anggota-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },
                            'Build Buku': {
                                dir('buku_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/buku-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },
                            'Build Peminjaman': {
                                dir('peminjaman_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/peminjaman-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },
                            'Build Pengembalian': {
                                dir('pengembalian_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/pengembalian-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },
                            'Build Gateway': {
                                dir('api-gateway-pustaka') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/api-gateway-pustaka:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            }
                        )
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo 'Deploying to Kubernetes Cluster...'

                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/00-storage.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/01-eureka.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/02-postgres.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/03-mongo.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/04-rabbitmq.yaml"
                    
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/05-elasticsearch.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/06-logstash.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/07-kibana.yaml"

                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/08-anggota-service.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/09-buku-service.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/10-peminjaman-service.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/11-pengembalian-service.yaml"
                    sh "kubectl apply -f ${K8S_MANIFEST_DIR}/12-api-gateway.yaml"
                    
                    echo 'Rolling out restarts to pick up new images...'
                    sh "kubectl rollout restart deployment/anggota-service"
                    sh "kubectl rollout restart deployment/buku-service"
                    sh "kubectl rollout restart deployment/peminjaman-service"
                    sh "kubectl rollout restart deployment/pengembalian-service"
                    sh "kubectl rollout restart deployment/api-gateway-pustaka"
                    sh "kubectl rollout restart deployment/logstash"
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment Kubernetes Berhasil! Image baru telah di-push dan cluster telah di-update.'
        }
        failure {
            echo '❌ BUILD/DEPLOY GAGAL!'
        }
    }
}