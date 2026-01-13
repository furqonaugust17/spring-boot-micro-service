# 📚 Spring Boot Microservices Project (Pustaka & Toko)

Project ini merupakan implementasi arsitektur **Microservices** menggunakan **Spring Boot 3 (Java 17)** dengan pendekatan **Hybrid Deployment**:

- 🚀 Infrastruktur dan Microservices Domain Pustaka dijalankan di **Kubernetes**  
- 🔄 CI/CD Pipeline dijalankan menggunakan **Jenkins**  
- 📊 Observability menggunakan **ELK Stack (Elasticsearch, Logstash, Kibana)**  
- 📨 Komunikasi Asinkron menggunakan **RabbitMQ**  
- 🗄️ Database menggunakan **PostgreSQL**, **MongoDB**, dan **H2**

Project ini bertujuan sebagai simulasi sistem enterprise microservices lengkap dari sisi:
- Deployment
- Monitoring
- Automation CI/CD
- Service Discovery
- Messaging

---

## 🏗️ Arsitektur Singkat

Komponen utama:

- **API Gateway** → Entry point request client
- **Eureka Server** → Service discovery
- **Microservices**:
  - anggota-service
  - buku-service
  - peminjaman-service
  - pengembalian-service
- **Messaging** → RabbitMQ
- **Database** → PostgreSQL, MongoDB, H2
- **Observability** → ELK Stack + Filebeat
- **CI/CD** → Jenkins Pipeline

---

## 📖 Dokumentasi Lengkap

Semua dokumentasi teknis tersedia di folder berikut:

👉 **[docs/README.md](docs/README.md)**

Di dalamnya terdapat panduan:
- Deployment Kubernetes
- Monitoring & Observability
- CI/CD Jenkins Pipeline
- Struktur sistem

---

## 👨‍💻 Author

**Furqon August Seventeenth**  
Mahasiswa Teknologi Rekayasa Perangkat Lunak Politeknik Negeri Padang