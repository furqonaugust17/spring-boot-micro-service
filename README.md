# Spring Boot Microservices Project

## 📌 Deskripsi
Project ini merupakan implementasi arsitektur **microservices** menggunakan **Spring Boot** sebagai bagian dari tugas kuliah. Sistem ini mengadopsi pola API Gateway sebagai titik masuk tunggal untuk mengakses grup service yang relevan.

Komunikasi dan penemuan service diatur oleh **Eureka Server**, sementara komunikasi asinkron pada layanan perpustakaan ditangani oleh **RabbitMQ**.

## 🏛️ Arsitektur Sistem
Sistem ini terbagi menjadi dua domain utama, masing-masing diakses melalui API Gateway-nya sendiri.

1.  **Domain Toko (Port `9000`)**
    * `api_gateway_toko`
        * `produk_service`
        * `pelanggan_service`
        * `order_service`

2.  **Domain Pustaka (Port `9002`)**
    * `api_gateway_pustaka`
        * `buku_service`
        * `anggota_service`
        * `peminjaman_service`
        * `pengembalian_service`
    * `rabbitmq-pustaka` (Service untuk notifikasi/messaging)

## 🏗️ Struktur Service
- **eureka_server** → Service registry sebagai penghubung antar service.
- **api-gateway** → Gateway untuk service terkait Toko (produk, pelanggan, order).
- **api-gateway-pustaka** → Gateway untuk service terkait Pustaka (buku, anggota, peminjaman).
- **produk_service** → Mengelola data produk.
- **pelanggan_service** → Mengelola data pelanggan.
- **order_service** → Mengelola data pemesanan.
- **buku_service** → Mengelola data buku.
- **anggota_service** → Mengelola data anggota.
- **peminjaman_service** → Mengelola data peminjaman.
- **pengembalian_service** → Mengelola data pengembalian.
- **rabbitmq-service** → Implementasi rabbitmq pada service order.
- **rabbitmq-pustaka** → Service untuk menangani message-broker dengan RabbitMQ untuk domain Pustaka.

---

## 🚀 Tahapan Menjalankan Project

1.  **Clone Repository:**
    ```bash
    git clone https://github.com/furqonaugust17/spring-boot-micro-service.git
    cd spring-boot-micro-service
    ```

2.  **Jalankan Eureka Server (Wajib Pertama):**
    ```bash
    cd eureka_server
    ./mvnw spring-boot:run
    ```
    Akses dashboard di `http://localhost:8761`. Pastikan Eureka berjalan sebelum melanjutkan.

3.  **Jalankan Semua Service Inti:**
    Buka terminal baru untuk setiap service dan jalankan perintah berikut di dalam direktori masing-masing (`produk_service`, `pelanggan_service`, `order_service`, `buku_service`, `anggota_service`, `peminjaman_service`, `pengembalian_service`, `rabbitmq-pustaka`).
    ```bash
    # Contoh untuk produk_service
    cd produk_service
    ./mvnw spring-boot:run
    ```
    Pastikan semua service telah terdaftar di dashboard Eureka.

4.  **Jalankan API Gateway:**
    Setelah semua service inti berjalan, jalankan kedua gateway di terminal terpisah.

    **Jalankan API Gateway Toko:**
    ```bash
    cd api_gateway
    ./mvnw spring-boot:run
    ```

    **Jalankan API Gateway Pustaka:**
    ```bash
    cd api-gateway-pustaka
    ./mvnw spring-boot:run
    ```

---

## 📡 Endpoint Service

Semua request sekarang harus melalui API Gateway.

### Gateway Toko (Base URL: `http://localhost:9000`)

#### 1. Produk Service
| Method | Endpoint                    | Deskripsi                         |
|--------|-----------------------------|-----------------------------------|
| POST   | `/api/product`       | Menambahkan produk baru           |
| GET    | `/api/product`       | Mendapatkan semua produk          |
| GET    | `/api/product/{id}`  | Mendapatkan produk berdasarkan ID |
| DELETE | `/api/product/{id}`  | Menghapus produk berdasarkan ID   |

#### 2. Pelanggan Service
| Method | Endpoint                      | Deskripsi                            |
|--------|-------------------------------|--------------------------------------|
| POST   | `/api/pelanggan`      | Menambahkan pelanggan baru           |
| GET    | `/api/pelanggan`      | Mendapatkan semua pelanggan          |
| GET    | `/api/pelanggan/{id}` | Mendapatkan pelanggan berdasarkan ID |
| DELETE | `/api/pelanggan/{id}` | Menghapus pelanggan berdasarkan ID   |

#### 3. Order Service
| Method | Endpoint                        | Deskripsi                         |
|--------|---------------------------------|-----------------------------------|
| POST   | `/api/order`              | Membuat order baru                |
| GET    | `/api/order`              | Mendapatkan semua order           |
| GET    | `/api/order/{id}`         | Mendapatkan order berdasarkan ID  |
| DELETE | `/api/order/{id}`         | Menghapus order berdasarkan ID    |
| GET    | `/api/order/product/{id}` | Mendapatkan order + product by ID |

---

### Gateway Pustaka (Base URL: `http://localhost:9002`)

#### 4. Anggota Service
| Method | Endpoint                    | Deskripsi                            |
|--------|-----------------------------|--------------------------------------|
| POST   | `/api/anggota`      | Menambahkan anggota baru           |
| GET    | `/api/anggota`      | Mendapatkan semua anggota          |
| GET    | `/api/anggota/{id}` | Mendapatkan anggota berdasarkan ID |
| DELETE | `/api/anggota/{id}` | Menghapus anggota berdasarkan ID   |

#### 5. Buku Service
| Method | Endpoint                | Deskripsi                         |
|--------|-------------------------|-----------------------------------|
| POST   | `/api/buku`        | Menambahkan buku baru           |
| GET    | `/api/buku`        | Mendapatkan semua buku          |
| GET    | `/api/buku/{id}`   | Mendapatkan buku berdasarkan ID |
| DELETE | `/api/buku/{id}`   | Menghapus buku berdasarkan ID   |

#### 6. Peminjaman Service
| Method | Endpoint                              | Deskripsi                                                 |
|--------|---------------------------------------|-----------------------------------------------------------|
| POST   | `/api/peminjaman`          | Menambahkan peminjaman baru                             |
| GET    | `/api/peminjaman`          | Mendapatkan semua peminjaman                            |
| GET    | `/api/peminjaman/{id}`     | Mendapatkan peminjaman berdasarkan ID                   |
| GET    | `/api/peminjaman/{id}/detail`| Mendapatkan peminjaman berdasarkan ID beserta detailnya |
| DELETE | `/api/peminjaman/{id}`     | Menghapus peminjaman berdasarkan ID                     |

#### 7. Pengembalian Service
| Method | Endpoint                                    | Deskripsi                                                     |
|--------|---------------------------------------------|---------------------------------------------------------------|
| POST   | `/api/pengembalian`            | Menambahkan pengembalian baru                               |
| GET    | `/api/pengembalian`            | Mendapatkan semua pengembalian                              |
| GET    | `/api/pengembalian/{id}`       | Mendapatkan pengembalian berdasarkan ID                     |
| GET    | `/api/pengembalian/{id}/detail`| Mendapatkan pengembalian berdasarkan ID beserta detailnya |
| DELETE | `/api/pengembalian/{id}`       | Menghapus pengembalian berdasarkan ID                       |

---

## ⚙️ Teknologi yang Digunakan
- Java 17+
- Spring Boot
- Spring Cloud Netflix Eureka
- **Spring Cloud Gateway**
- **RabbitMQ**
- Maven
- REST API