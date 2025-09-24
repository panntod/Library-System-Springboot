# Library System (Spring Boot)

Sebuah proyek sederhana menggunakan **Spring Boot** untuk mengelola sistem perpustakaan (library).  
Proyek ini dibuat untuk mengimplementasikan pengetahuan Spring Boot melalui kasus nyata.

## 💡 Fitur Utama

- Manajemen buku (CRUD buku: tambah, edit, hapus, lihat)  
- Manajemen pengguna (pengguna / anggota perpustakaan)  
- Peminjaman dan pengembalian buku  
- Validasi input dan penanganan error  
- API RESTful dengan JSON  
- Autentikasi / otorisasi (jika ada)
- Sorting dan Pagination data
- RBAC (Role Based Access Control)

## 🧰 Teknologi & Dependensi

| Komponen | Versi / Keterangan |
|---------|---------------------|
| Spring Boot | (sesuaikan versi di `pom.xml`) |
| Spring Data JPA | untuk akses database |
| H2 / MySQL / PostgreSQL | (Database, sesuaikan konfigurasi) |
| Maven | sebagai build tool |
| Lombok | untuk mengurangi boilerplate code |
| (Opsional) Spring Security | jika ada autentikasi / otorisasi |

## 🧩 Struktur Proyek

Berikut ringkasan struktur direktori utama:

```
└── src
├── main
│   ├── java
│   │   └── com.example.library  ← paket utama
│   │       ├── controller       ← kelas controller / API
│   │       ├── service          ← kelas layanan / bisnis logika
│   │       ├── repository       ← interface JPA Repository
│   │       ├── model            ← entitas / domain class
│   │       └── dto / exception  ← (opsional) DTO & kelas exception
│   └── resources
│       ├── application.properties (atau application.yml)
│       └── data / schema (opsional)
└── test
└── java / resources

````

## 🚀 Cara Menjalankan

Ikuti langkah-langkah berikut agar aplikasi bisa berjalan:

1. Clone repositori ini  
```bash
   git clone https://github.com/panntod/Library-System-Springboot.git
   cd Library-System-Springboot
```

2. Konfigurasi database
   Di `src/main/resources/application.properties` (atau `.yml`), sesuaikan:

   * JDBC URL
   * Username / password
   * Dialect (jika menggunakan MySQL / PostgreSQL)

3. Build & Run

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   Atau jika kamu menggunakan IDE (IntelliJ IDEA / Eclipse) langsung jalankan kelas utama `Application`.

4. Akses aplikasi
   Buka di browser / tools seperti Postman, insomia, dsb:

   ```
   http://localhost:8080/
   ```

   (Sesuaikan port jika kamu sudah ubah di konfigurasi)

## 📦 API Endpoints

| HTTP Method | Endpoint        | Kegunaan                        |
|-------------|-----------------|---------------------------------|
| GET         | `/user`         | Daftar semua user               |
| POST        | `/user`         | Tambah user baru                |
| PUT         | `/user/{id}`    | Ubah data user berdasarkan ID   |
| PATCH       | `/user/{id}`    | Update sebagian data user       |
| DELETE      | `/user/{id}`    | Hapus user berdasarkan ID       |
| GET         | `/books`        | Daftar semua buku               |
| GET         | `/books/{id}`   | Detail buku berdasarkan ID      |
| POST        | `/books`        | Tambah buku baru                |
| PUT         | `/books/{id}`   | Ubah data buku berdasarkan ID   |
| DELETE      | `/books/{id}`   | Hapus buku berdasarkan ID       |
| POST        | `/borrow`       | Pinjam buku                     |
| POST        | `/return`       | Kembalikan buku                 |

## ✅ Kebutuhan Minimum

* Java 11+ (atau versi sesuai proyek)
* Maven
* Database (H2 embedded atau eksternal)
* IDE / editor yang mendukung Spring Boot

## 🚧 Catatan & Pengembangan ke Depan

* Menambahkan halaman frontend (React, Angular, atau Thymeleaf)
* Menambahkan dokumentasi API menggunakan Swagger / OpenAPI
* Menambahkan validasi input yang lebih ketat & penanganan error global
* Menambahkan unit test & integrasi test

## 💬 Kontribusi

Jika kamu ingin memberikan kontribusi:

1. Fork repositori ini
2. Buat branch fitur / perbaikan: `feature/nama-fitur`
3. Commit perubahan & push
4. Ajukan Pull Request, jelaskan perubahanmu
