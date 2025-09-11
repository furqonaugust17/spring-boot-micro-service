# Spring Boot Microservices Project

## 📌 Deskripsi
Project ini merupakan implementasi arsitektur **microservices** menggunakan **Spring Boot** sebagai bagian dari tugas kuliah.  
Sistem ini terdiri dari beberapa service yang saling terhubung melalui **Eureka Server** sebagai service registry.  

### Struktur Service
- **eureka_server** → Service registry sebagai penghubung antar service.  
- **order_service** → Mengelola data pemesanan.  
- **produk_service** → Mengelola data produk.  
- **pelanggan_service** → Mengelola data pelanggan.  
- **anggota_service** → Mengelola data anggota.  
- **buku_service** → Mengelola data buku.  
- **peminjaman_service** → Mengelola data peminjaman.  
- **pengembalian_service** → Mengelola data pengembalian.  

---

## 🚀 Tahapan Menjalankan Project

1. Clone repository:
   ```bash
   git clone https://github.com/furqonaugust17/spring-boot-micro-service.git
   cd spring-boot-micro-service
   ```

2. Jalankan **Eureka Server** terlebih dahulu:
   ```bash
   cd eureka_server
   ./mvnw spring-boot:run
   ```

3. Jalankan service yang diperlukan:

   Jalankan **Order Service**:
   ```bash
   cd order_service
   ./mvnw spring-boot:run
   ```

   Jalankan **Produk Service**:
   ```bash
   cd produk_service
   ./mvnw spring-boot:run
   ```

   Jalankan **Pelanggan Service**:
   ```bash
   cd pelanggan_service
   ./mvnw spring-boot:run
   ```

   Jalankan **Anggota Service**:
   ```bash
   cd anggota_service
   ./mvnw spring-boot:run
   ```

   Jalankan **Peminjaman Service**:
   ```bash
   cd peminjaman_service
   ./mvnw spring-boot:run
   ```

   Jalankan **Pengembalian Service**:
   ```bash
   cd pengembalian_service
   ./mvnw spring-boot:run
   ```

4. Akses **Eureka Dashboard** di browser:
   ```
   http://localhost:8761
   ```

---

## 📡 Endpoint Service

### 1. Order Service
Base URL: `http://localhost:8083/`

| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/order`              | Membuat order baru               |
| GET    | `/api/order`              | Mendapatkan semua order          |
| GET    | `/api/order/{id}`         | Mendapatkan order berdasarkan ID |
| DELETE | `/api/order/{id}`         | Menghapus order berdasarkan ID   |
| GET    | `/api/order/product/{id}` | Mendapatkan order + product by ID |

**Contoh Body POST**
```json
{
  "productId": Long,
  "pelangganId": Long,
  "jumlah": Integer,
  "tanggal": String,
  "status": String,
  "total": Double
}
```

---

### 2. Produk Service
Base URL: `http://localhost:8081/`

| Method | Endpoint            | Deskripsi                         |
|--------|---------------------|-----------------------------------|
| POST   | `/api/product`      | Menambahkan produk baru           |
| GET    | `/api/product`      | Mendapatkan semua produk          |
| GET    | `/api/product/{id}` | Mendapatkan produk berdasarkan ID |
| DELETE | `/api/product/{id}` | Menghapus produk berdasarkan ID   |

**Contoh Body POST**
```json
{
  "nama": String,
  "satuan": String,
  "harga": Double
}
```

---

### 3. Pelanggan Service
Base URL: `http://localhost:8082/`

| Method | Endpoint               | Deskripsi                            |
|--------|------------------------|--------------------------------------|
| POST   | `/api/pelanggan`       | Menambahkan pelanggan baru           |
| GET    | `/api/pelanggan`       | Mendapatkan semua pelanggan          |
| GET    | `/api/pelanggan/{id}`  | Mendapatkan pelanggan berdasarkan ID |
| DELETE | `/api/pelanggan/{id}`  | Menghapus pelanggan berdasarkan ID   |

**Contoh Body POST**
```json
{
  "kode": String,
  "nama": String,
  "alamat": String
}
```

---

### 4. Anggota Service
Base URL: `http://localhost:8084/`

| Method | Endpoint               | Deskripsi                            |
|--------|------------------------|--------------------------------------|
| POST   | `/api/anggota`       | Menambahkan anggota baru           |
| GET    | `/api/anggota`       | Mendapatkan semua anggota          |
| GET    | `/api/anggota/{id}`  | Mendapatkan anggota berdasarkan ID |
| DELETE | `/api/anggota/{id}`  | Menghapus anggota berdasarkan ID   |

**Contoh Body POST**
```json
{
  "nim": String,
  "nama": String,
  "alamat": String,
  "jenisKelamin":String
}
```

---

### 5. Buku Service
Base URL: `http://localhost:8085/`

| Method | Endpoint               | Deskripsi                            |
|--------|------------------------|--------------------------------------|
| POST   | `/api/buku`       | Menambahkan buku baru           |
| GET    | `/api/buku`       | Mendapatkan semua buku          |
| GET    | `/api/buku/{id}`  | Mendapatkan buku berdasarkan ID |
| DELETE | `/api/buku/{id}`  | Menghapus buku berdasarkan ID   |

**Contoh Body POST**
```json
{
  "judul": String,
  "pengarang": String,
  "penerbit": String,
  "tahunTerbit":String
}
```

---

### 6 Peminjaman Service
Base URL: `http://localhost:8086/`

Format tanggal pinjam dan tanggal kembali dd-MM-yyyy.
```json
{
  "tanggalPinjam": "12-08-2025",
  "tanggalKembali": "15-08-2025",
}
```

| Method | Endpoint               | Deskripsi                            |
|--------|------------------------|--------------------------------------|
| POST   | `/api/peminjaman`       | Menambahkan peminjaman baru           |
| GET    | `/api/peminjaman`       | Mendapatkan semua peminjaman          |
| GET    | `/api/peminjaman/{id}`  | Mendapatkan peminjaman berdasarkan ID |
| GET    | `/api/peminjaman/{id}/detail`  | Mendapatkan peminjaman berdasarkan ID beserta detailnya |
| DELETE | `/api/peminjaman/{id}`  | Menghapus peminjaman berdasarkan ID   |

**Contoh Body POST**
```json
{
  "tanggalPinjam": String,
  "tanggalKembali": String,
  "anggotaId": Long,
  "bukuId": Long
}
```

---

### 7 Pengembalian Service
Base URL: `http://localhost:8087/`

Format tanggal dikembalikan dd-MM-yyyy.
```json
{
  "tanggalDikembalikan": "12-08-2025",
}
```

| Method | Endpoint               | Deskripsi                            |
|--------|------------------------|--------------------------------------|
| POST   | `/api/pengembalian`       | Menambahkan pengembalian baru           |
| GET    | `/api/pengembalian`       | Mendapatkan semua pengembalian          |
| GET    | `/api/pengembalian/{id}`  | Mendapatkan pengembalian berdasarkan ID |
| GET    | `/api/pengembalian/{id}/detail`  | Mendapatkan pengembalian berdasarkan ID beserta detailnya |
| DELETE | `/api/pengembalian/{id}`  | Menghapus pengembalian berdasarkan ID   |

**Contoh Body POST**
```json
{
  "peminjamanId": Long,
  "tanggalDikembalikan": String
}
```

---

## ⚙️ Teknologi yang Digunakan
- Java 17+  
- Spring Boot 3.5.5
- Spring Cloud Netflix Eureka  
- Maven  
- REST API  
