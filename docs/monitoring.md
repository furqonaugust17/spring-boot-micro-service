# 📊 Monitoring & Observability

Sistem monitoring pada project ini menggunakan kombinasi:

- ✅ **Prometheus** → Metrics collection
- ✅ **Grafana** → Metrics visualization
- ✅ **ELK Stack** → Log management
- ✅ **Filebeat** → Log shipper

Monitoring digunakan untuk memantau:
- Kesehatan service
- Resource usage
- Performa aplikasi
- Centralized logging

---

## 🧩 Komponen Monitoring

### 📈 Metrics Stack

| Komponen | Fungsi |
|-----------|--------|
| Prometheus | Mengumpulkan metrics dari service |
| Grafana | Visualisasi metrics |
| Spring Boot Actuator | Expose metrics endpoint |
| ServiceMonitor | Integrasi Kubernetes |

Spring Boot service mengekspos metrics melalui endpoint:

```bash
/actuator/prometheus
```

Konfigurasi pada application:

```properties
management.endpoints.web.exposure.include=health,info,prometheus
```

### 📜 Logging Stack (ELK)
|Komponen | Fungsi|
|-----------|--------|
|Elasticsearch | Penyimpanan log|
|Logstash | Processing log|
|Kibana | Visualisasi log|
|Filebeat | Pengirim log|

### 🔄 Alur Metrics
1. Spring Boot expose /actuator/prometheus
2. Prometheus melakukan scraping metrics
3. Metrics disimpan di Prometheus
4. Grafana membaca metrics dari Prometheus
5. Dashboard menampilkan grafik performa

### 🔄 Alur Logging
1. Microservice menghasilkan log
2. Filebeat membaca file log
3. Log dikirim ke Logstash
4. Log disimpan ke Elasticsearch
5. Kibana menampilkan log terpusat

---

## 🚀 Deployment Monitoring di Kubernetes

Monitoring dideploy menggunakan manifest:
File | Fungsi
|-----------|--------|
|14-values-monitoring.yml | Konfigurasi monitoring|
|15-service-monitor.yml | Service monitor|

Apply:
```bash
kubectl apply -f k8s/14-values-monitoring.yml
kubectl apply -f k8s/15-service-monitor.yml
```

## 🌐 Akses Dashboard

### 🔎 Prometheus

```bash
kubectl get svc prometheus
```
Gunakan NodePort / Port Forward.

### 📊 Grafana
```bash
kubectl get svc grafana
```

Login default:

```bash
username: admin
password: admin
```

### 📑 Kibana

```bash
kubectl get svc kibana
```

---

# 🎯 Manfaat Monitoring
1. Melihat CPU, Memory, Request
2. Mendeteksi service down
3. Analisa bottleneck
4. Observasi traffic realtime