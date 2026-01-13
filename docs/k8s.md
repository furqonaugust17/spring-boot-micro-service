# 🚀 Deployment Kubernetes

Dokumen ini menjelaskan proses deployment seluruh infrastruktur dan microservices pustaka ke dalam cluster Kubernetes.


## 📁 Struktur Folder

Folder `k8s/` berisi manifest Kubernetes berikut:

| File | Fungsi |
|------|--------|
| 00-storage.yml | Persistent Volume & PVC |
| 01-eureka.yml | Eureka Server |
| 02-postgres.yml | PostgreSQL |
| 03-mongo.yml | MongoDB |
| 04-rabbitmq.yml | RabbitMQ |
| 05-elasticsearch.yml | Elasticsearch |
| 06-logstash.yml | Logstash |
| 07-kibana.yml | Kibana |
| 08-anggota-service.yml | Anggota Service |
| 09-buku-service.yml | Buku Service |
| 10-peminjaman-service.yml | Peminjaman Service |
| 11-pengembalian-service.yml | Pengembalian Service |
| 12-rabbitmq-email-service.yml | Email Consumer |
| 13-api-gateway.yml | API Gateway |
| 14-values-monitoring.yml | Konfigurasi monitoring |
| 15-service-monitor.yml | Service monitor |


## ✅ Prasyarat

- Docker
- Kubernetes aktif
- kubectl terkonfigurasi
- Repository sudah di-clone

```bash
git clone https://github.com/furqonaugust17/spring-boot-micro-service.git
cd spring-boot-micro-service
```
## ▶️ Langkah Deployment
### 1️⃣ Deploy Infrastruktur
```bash
kubectl apply -f k8s/00-storage.yml
kubectl apply -f k8s/01-eureka.yml
kubectl apply -f k8s/02-postgres.yml
kubectl apply -f k8s/03-mongo.yml
kubectl apply -f k8s/04-rabbitmq.yml
kubectl apply -f k8s/05-elasticsearch.yml
kubectl apply -f k8s/06-logstash.yml
kubectl apply -f k8s/07-kibana.yml
```

### 2️⃣ Deploy Microservices
```bash
kubectl apply -f k8s/08-anggota-service.yml
kubectl apply -f k8s/09-buku-service.yml
kubectl apply -f k8s/10-peminjaman-service.yml
kubectl apply -f k8s/11-pengembalian-service.yml
kubectl apply -f k8s/12-rabbitmq-email-service.yml
kubectl apply -f k8s/13-api-gateway.yml
```

### 🔍 Verifikasi

Cek status pod:
```bash
kubectl get pods
```

Cek service:
```bash
kubectl get svc
```
### 🌐 Akses Layanan
Service | Port
--- | --- |
Eureka | 8761
API Gateway | 9002
Kibana | NodePort
