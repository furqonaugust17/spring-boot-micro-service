pipeline {
    agent any

    tools {
        maven 'Maven-3' 
        jdk 'jdk-17' 
    }

    environment {
        COMPOSE_FILE = 'docker-compose-pustaka.yml'
        SKIP_TESTS = '-DskipTests'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git...'
                checkout scm
            }
        }

        stage('Build All JARs') {
            steps {
                script {
                    echo 'Building anggota-service...'
                    dir('anggota_service') { 
                        sh "mvn clean package ${env.SKIP_TESTS}" 
                    }
                    
                    echo 'Building buku-service...'
                    dir('buku-service') {
                        sh "mvn clean package ${env.SKIP_TESTS}"
                    }
                    
                    echo 'Building peminjaman-service...'
                    dir('peminjaman_service') {
                        sh "mvn clean package ${env.SKIP_TESTS}"
                    }
                    
                    echo 'Building pengembalian-service...'
                    dir('pengembalian_service') {
                        sh "mvn clean package ${env.SKIP_TESTS}" 
                    }
                    
                    echo 'Building api-gateway-pustaka...'
                    dir('api-gateway-pustaka') {
                        sh "mvn clean package ${env.SKIP_TESTS}"
                    }
                }
            }
        }

        stage('Build & Deploy Containers') {
            echo 'Starting parallel deployment using Docker Compose...'
            
            parallel {
                
                stage('Deploy Anggota Service') {
                    steps {
                        sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps anggota-service"
                    }
                }
                
                stage('Deploy Buku Service') {
                    steps {
                        sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps buku-service"
                    }
                }
                
                stage('Deploy Peminjaman Service') {
                    steps {
                        sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps peminjaman-service"
                    }
                }
                
                stage('Deploy Pengembalian Service') {
                    steps {
                        sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps pengembalian-service"
                    }
                }
                
                stage('Deploy API Gateway') {
                    steps {
                        sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps api-gateway-pustaka"
                    }
                }
                
            }
        }
    }
    
    post {
        always {
            cleanWs()
            sh 'docker image prune -f'
        }
        success {
            echo '✅ Deployment berhasil! Semua Microservices telah diperbarui.'
        }
        failure {
            echo '❌ BUILD GAGAL! Periksa log untuk mengetahui service mana yang error.'
        }
    }
}