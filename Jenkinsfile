pipeline {
    agent any

    tools {
        maven 'Maven-3' 
        jdk 'jdk-17' 
    }

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
        SKIP_TESTS = '-DskipTests'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git...'
                checkout scm
            }
        }

        stage('Start Infrastructure') {
            steps {
                echo 'Starting Database, Queue, dan ELK (Tanpa Rebuild)...'
                sh '''
                    docker compose -f ${COMPOSE_FILE} up -d --no-build \
                        eureka-pustaka \
                        postgres-pustaka \
                        mongo-pustaka \
                        rabbitmq-service \
                        elasticsearch \
                        logstash \
                        kibana
                '''
                echo 'Menunggu 30 detik agar semua service infrastruktur siap...'
                sleep 30
            }
        }

        stage('Build All JARs') {
            steps {
                script {
                    echo 'Building Microservices JAR files...'
                    dir('anggota_service') { sh "mvn clean package ${env.SKIP_TESTS}" }
                    dir('buku_service') { sh "mvn clean package ${env.SKIP_TESTS}" }
                    dir('peminjaman_service') { sh "mvn clean package ${env.SKIP_TESTS}" }
                    dir('pengembalian_service') { sh "mvn clean package ${env.SKIP_TESTS}" }
                    dir('api-gateway-pustaka') { sh "mvn clean package ${env.SKIP_TESTS}" }
                }
            }
        }

        stage('Build & Deploy Containers') {
            parallel {
                
                'Deploy Anggota Service' : {
                    steps { sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps anggota-service" } 
                },
                
                'Deploy Buku Service' : {
                    steps { sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps buku-service" } 
                },
                
                'Deploy Peminjaman Service' : {
                    steps { sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps peminjaman-service" } 
                },
                
                'Deploy Pengembalian Service' : {
                    steps { sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps pengembalian-service" } 
                },
                
                'Deploy API Gateway' : {
                    steps { sh "docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps api-gateway-pustaka" } 
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
            echo '❌ BUILD GAGAL!'
        }
    }
}