# 📚 Spring Boot Microservices Project (Pustaka & Toko)

## 📌 Deskripsi

Project ini adalah implementasi arsitektur **Microservices** menggunakan **Spring Boot 3** (Java 17). Arsitektur ini mencakup Domain Aplikasi (Pustaka & Toko) dan Infrastruktur yang terbagi: **CI/CD** dikelola oleh Docker Compose, sementara **Layanan Pustaka** diorkestrasi oleh Kubernetes.

**Teknologi Utama yang Diimplementasikan:**

* **Orkestrasi Aplikasi:** Kubernetes (K8s)
* **CI/CD Engine:** Jenkins (via Docker Compose)
* **Service Discovery:** Eureka Server
* **Messaging:** RabbitMQ
* **Database:** PostgreSQL (Relasional) dan MongoDB (NoSQL)
* **Observability:** ELK Stack (Elasticsearch, Logstash, Kibana) dengan Filebeat Sidecar.

## 🏛️ Arsitektur Pustaka Service (Hybrid Deployment)

Sistem ini menggunakan model **Hybrid Deployment**: Jenkins dan Docker berjalan di Host/VM, sementara Microservices Pustaka dan Infrastruktur pendukungnya berjalan di dalam Cluster Kubernetes.



### A. Infrastruktur Inti (Berjalan di Kubernetes)

| Komponen | Tujuan | Teknologi | Port Akses (Internal) |
| :--- | :--- | :--- | :--- |
| **`eureka-pustaka`** | Service Discovery & Registry. | Spring Cloud Netflix Eureka | `8761` |
| **`postgres-pustaka`** | Database Relasional Utama. | PostgreSQL | `5432` |
| **`mongo-pustaka`** | Database Dokumen. | MongoDB | `27017` |
| **`rabbitmq-service`** | Message Broker Asinkron. | RabbitMQ | `5672` |

### B. Observability (ELK Stack - Berjalan di Kubernetes)

Semua Microservices Domain Pustaka diinstrumentasi dengan **Filebeat Sidecar** untuk mengirimkan log ke ELK Stack, memungkinkan pengawasan terpusat.

| Komponen | Tujuan | Teknologi | Port Akses (Eksternal) |
| :--- | :--- | :--- | :--- |
| **`elasticsearch`** | Penyimpanan dan Indeks Log. | Elastic Search | `9200` |
| **`logstash`** | Pemrosesan Log dari Filebeat ke Elastic. | Logstash | `5044` |
| **`kibana`** | Visualisasi dan Analisis Log. | Kibana | `NodePort: 3xxxx` |

### C. Domain Pustaka (Microservices - Berjalan di Kubernetes)

* **`anggota-service`**
* **`buku-service`**
* **`peminjaman-service`**
* **`pengembalian-service`**
* **`api-gateway-pustaka`** (Port **`9002`**)

### D. CI/CD Pipeline (Berjalan di Docker Compose)

* **`jenkins`** : CI/CD Engine yang berjalan di Docker Compose. Bertanggung jawab untuk **Build Maven**, **Build & Push Docker Image** ke Registry, dan **Deploy/Rollout** ke Kubernetes menggunakan `kubectl` yang terpasang (mounted) di dalamnya.

---

## 🚀 Tahapan Menjalankan Project

Tahapan ini memastikan semua infrastruktur Kubernetes aktif sebelum menjalankan Jenkins.

### Prasyarat

1.  **Docker & Kubernetes:** Pastikan Docker Desktop berjalan dan Kubernetes sudah diaktifkan.
2.  **`kubectl`:** Sudah terinstal dan terkonfigurasi pada Host.
3.  **Docker Compose:** Terinstal pada Host.
4.  **Kubeconfig Mount:** Pastikan file `docker-compose.yml` untuk Jenkins sudah diatur untuk me-mount folder `.kube` Host agar `kubectl` dapat terautentikasi ke Cluster Kubernetes (lihat konfigurasi volume di `docker-compose.yml`).
5.  **Jenkins Credentials:** Credential Docker Registry (`docker-hub-credentials`) harus dikonfigurasi di Jenkins.

### A. Deployment Infrastruktur ke Kubernetes (Wajib Pertama)

Jalankan perintah ini di terminal Host Anda yang terhubung ke cluster Kubernetes.

1.  **Clone Repository:**
    ```bash
    git clone [https://github.com/furqonaugust17/spring-boot-micro-service.git](https://github.com/furqonaugust17/spring-boot-micro-service.git)
    cd spring-boot-micro-service
    ```

2.  **Terapkan PVCs dan Infrastruktur Pustaka:**
    ```bash
    kubectl apply -f k8s-manifests/00-storage.yaml # Persistent Volume Claims
    kubectl apply -f k8s-manifests/01-eureka.yaml 
    kubectl apply -f k8s-manifests/02-postgres.yaml
    kubectl apply -f k8s-manifests/03-mongo.yaml
    kubectl apply -f k8s-manifests/04-rabbitmq.yaml
    kubectl apply -f k8s-manifests/05-elasticsearch.yaml
    kubectl apply -f k8s-manifests/06-logstash.yaml
    kubectl apply -f k8s-manifests/07-kibana.yaml
    ```
    (Microservices akan dideploy selanjutnya melalui Jenkins Pipeline.)

### B. Menjalankan CI/CD Engine (Jenkins)

Jalankan Jenkins sebagai kontainer Docker untuk memulai proses CI/CD.

```bash
docker compose up -d jenkins
```

### C. Melakukan Deployment Microservices

1.  Akses Jenkins Dashboard di `http://localhost:8088`.
2.  Jalankan Pipeline CI/CD yang bertanggung jawab untuk:
      * Build Maven JARs.
      * Build & Push Docker Images Microservices Pustaka.
      * `kubectl apply` manifest Microservices ke Kubernetes.
      * `kubectl rollout restart` untuk memicu pembaruan Pod.

### D. Akses Layanan Eksternal

Akses layanan menggunakan IP Host Anda dan port yang dialokasikan oleh Kubernetes/Docker Compose.

| Layanan | Lokasi Deployment | Port Akses | Perintah Cek Akses |
| :--- | :--- | :--- | :--- |
| **Jenkins Dashboard** | Docker Compose | `8088` | `http://localhost:8088` |
| **API Gateway Pustaka** | Kubernetes | `9002` | `kubectl get svc api-gateway-pustaka` (Gunakan EXTERNAL-IP/NodeIP) |
| **Kibana Dashboard** | Kubernetes | `5601` | `kubectl get svc kibana` (Lihat NodePort yang dialokasikan) |

-----

## 💻 Domain Toko (Manual Deployment)

Domain Toko masih menggunakan cara deployment lokal (`mvn spring-boot:run`) dan terhubung ke Eureka yang berjalan di Kubernetes.

1.  **Jalankan Service Toko:**
    Jalankan service Toko (`produk_service`, `pelanggan_service`, `order_service`) secara terpisah. **Pastikan mereka dikonfigurasi untuk mendaftar ke Eureka yang berada di Kubernetes Cluster.**

2.  **Jalankan API Gateway Toko:**
    Akses Gateway Toko di `http://localhost:9000`.

-----

## 📡 Endpoint Service

Semua request harus melalui API Gateway.

### 1\. Gateway Toko (Base URL: `http://localhost:9000`)

| Method | Endpoint | Deskripsi |
| :--- | :--- | :--- |
| POST | `/api/product` | Menambahkan produk baru |
| GET | `/api/product` | Mendapatkan semua produk |
| GET | `/api/pelanggan` | Mendapatkan semua pelanggan |
| POST | `/api/order` | Membuat order baru |

### 2\. Gateway Pustaka (Base URL: `http://localhost:9002`)

#### Anggota Service

| Method | Endpoint | Deskripsi |
| :--- | :--- | :--- |
| POST | `/api/anggota` | Menambahkan anggota baru |
| GET | `/api/anggota` | Mendapatkan semua anggota |
| GET | `/api/anggota/{id}` | Mendapatkan anggota berdasarkan ID |
| DELETE | `/api/anggota/{id}` | Menghapus anggota berdasarkan ID |

#### Buku Service

| Method | Endpoint | Deskripsi |
| :--- | :--- | :--- |
| POST | `/api/buku` | Menambahkan buku baru |
| GET | `/api/buku` | Mendapatkan semua buku |
| GET | `/api/buku/{id}` | Mendapatkan buku berdasarkan ID |
| DELETE | `/api/buku/{id}` | Menghapus buku berdasarkan ID |

#### Peminjaman Service

| Method | Endpoint | Deskripsi |
| :--- | :--- | :--- |
| POST | `/api/peminjaman` | Menambahkan peminjaman baru |
| GET | `/api/peminjaman` | Mendapatkan semua peminjaman |
| GET | `/api/peminjaman/{id}` | Mendapatkan peminjaman berdasarkan ID |
| DELETE | `/api/peminjaman/{id}` | Menghapus peminjaman berdasarkan ID |

#### Pengembalian Service

| Method | Endpoint | Deskripsi |
| :--- | :--- | :--- |
| POST | `/api/pengembalian` | Menambahkan pengembalian baru |
| GET | `/api/pengembalian` | Mendapatkan semua pengembalian |
| GET | `/api/pengembalian/{id}` | Mendapatkan pengembalian berdasarkan ID |
| GET | `/api/pengembalian/{id}/detail` | Mendapatkan pengembalian berdasarkan ID beserta detailnya |
| DELETE | `/api/pengembalian/{id}` | Menghapus pengembalian berdasarkan ID |


testing migration jenkins