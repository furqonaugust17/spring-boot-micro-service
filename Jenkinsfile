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

        stage('Start Infrastructure') {
            steps {
                echo 'Starting Database, Queue, dan ELK...'
                sh '''
                    docker compose -f ${env.COMPOSE_FILE} rm -sf \
                        eureka-pustaka \
                        postgres-pustaka \
                        mongo-pustaka \
                        rabbitmq-service \
                        elasticsearch \
                        logstash \
                        kibana
                    docker compose -f ${env.COMPOSE_FILE} up -d --build \
                        eureka-pustaka \
                        postgres-pustaka \
                        mongo-pustaka \
                        rabbitmq-service \
                        elasticsearch \
                        logstash \
                        kibana
                '''
                echo 'Menunggu infrastruktur siap (30 detik)...'
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

                stage('Deploy Anggota Service') {
                    steps { 
                        sh '''
                        docker compose -f ${env.COMPOSE_FILE} rm -sf anggota-service filebeat-anggota
                        docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps anggota-service filebeat-anggota
                        '''
                    }
                }

                stage('Deploy Buku Service') {
                    steps { 
                        sh '''
                        docker compose -f ${env.COMPOSE_FILE} rm -sf buku-service filebeat-buku
                        docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps buku-service filebeat-buku
                        '''
                    }
                }

                stage('Deploy Peminjaman Service') {
                    steps { 
                        sh '''
                        docker compose -f ${env.COMPOSE_FILE} rm -sf peminjaman-service filebeat-peminjaman
                        docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps peminjaman-service filebeat-peminjaman
                        '''
                    }
                }

                stage('Deploy Pengembalian Service') {
                    steps { 
                        sh '''
                        docker compose -f ${env.COMPOSE_FILE} rm -sf pengembalian-service filebeat-pengembalian
                        docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps pengembalian-service filebeat-pengembalian
                        '''
                    }
                }

                stage('Deploy API Gateway') {
                    steps { 
                        sh '''
                        docker compose -f ${env.COMPOSE_FILE} rm -sf api-gateway-pustaka filebeat-gateway
                        docker compose -f ${env.COMPOSE_FILE} up -d --build --no-deps api-gateway-pustaka filebeat-gateway
                        '''
                    }
                }

            }
        }
    }

    post {
        success {
            echo '✅ Deployment berhasil! Semua Microservices telah diperbarui.'
        }
        failure {
            echo '❌ BUILD GAGAL!'
        }
    }
}
