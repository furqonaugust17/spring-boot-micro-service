pipeline {
    agent any

    tools {
        maven 'Maven-3' 
    }

    environment {
        COMPOSE_FILE = 'docker-compose-pustaka.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JARs') {
            steps {
                sh 'mvn clean package -DskipTests' 
            }
        }

        stage('Build & Deploy Containers') {
            parallel {
                stage('Anggota Service') {
                    steps {
                        script {
                            sh 'docker compose -f ${COMPOSE_FILE} up -d --build --no-deps anggota-service'
                        }
                    }
                }
                stage('Buku Service') {
                    steps {
                        script {
                            sh 'docker compose -f ${COMPOSE_FILE} up -d --build --no-deps buku-service'
                        }
                    }
                }
                stage('Peminjaman Service') {
                    steps {
                        script {
                            sh 'docker compose -f ${COMPOSE_FILE} up -d --build --no-deps peminjaman-service'
                        }
                    }
                }
                stage('Pengembalian Service') {
                    steps {
                        script {
                            sh 'docker compose -f ${COMPOSE_FILE} up -d --build --no-deps pengembalian-service'
                        }
                    }
                }
            }
        }
        
        stage('Cleanup') {
             steps {
                 sh 'docker image prune -f'
             }
        }
    }
}