# Jejakin
[![Log Search CI](https://github.com/dhanyn10/tes-jejakin-be/actions/workflows/log-search.yml/badge.svg)](https://github.com/dhanyn10/tes-jejakin-be/actions/workflows/log-search.yml)
[![Order CI](https://github.com/dhanyn10/tes-jejakin-be/actions/workflows/order.yml/badge.svg)](https://github.com/dhanyn10/tes-jejakin-be/actions/workflows/order.yml)
## Spesifikasi
- Maven
    ```sh
    $ mvn --version
    Apache Maven 3.6.3
    Maven home: /usr/share/maven
    Java version: 17.0.5, vendor: Private Build, runtime: /usr/lib/jvm/java-17-openjdk-amd64
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "5.15.0-41-generic", arch: "amd64", family: "unix"
    ```
- Java
    ```sh
    $ java --version
    openjdk 17.0.5 2022-10-18
    OpenJDK Runtime Environment (build 17.0.5+8-Ubuntu-2ubuntu122.04)
    OpenJDK 64-Bit Server VM (build 17.0.5+8-Ubuntu-2ubuntu122.04, mixed mode, sharing)
    ```
- gunakan eclipse untuk membuka project. Pilih `File > open project from file system`
### LogSearch
1. untuk menjalankan `LogSearch`, pilih project, lalu klik tombol **RUN** pada eclipse.
2. masukkan keyword yang ingin dicari, pisahkan dengan tanda koma. Contoh:
    ```
    google,Apple
    ```
3. Setelah menentukan keyword, akan muncul tampilan `window dialog` yang bisa digunakan untuk menentukan letak file log. Gunakan `window dialog` ini untuk memilih file log yang akan digunakan.
4. Setelah memilih file log, program akan mengeksekusi perintah yang dibutuhkan, yaitu menghitung jumlah keyword yang muncul berdasarkan keyword yang dicari. Program ini __case sensitive__ sehingga perbedaan huruf kapital akan berpengaruh pada hasil pencarian.

### Order
#### Run
##### Tanpa Docker
1. ubah __database host__ `application.properties` yang berada di `order > src > main > resources`
    dari
    ```
    spring.data.mongodb.host=mongo
    ```
    menjadi
    ```
    spring.data.mongodb.host=localhost
    ```
2. siapkan database mongodb sesuai dengan nama database yang diinginkan, contohnya `jejakin`
3. untuk menjalankan `order`, pilih project, lalu klik tombol **RUN** pada eclipse.
##### Dengan Docker
1. pastikan docker desktop sudah terinstall dan running, dan docker compose sudah diinstall di sistem
2. jalankan program dengan command
    ```sh
    docker-compose up -d
    ```
### Petunjuk penggunaan Rest API
dibutuhkan postman saat menggunakan Rest API
#### Users
- generate admin => `GET` pada alamat `localhost:8080/users/admin` untuk melakukan auto negerate user admin dengan role admin. Hanya user dengan **role admin** yang bisa memembuat user baru dengan role admin atau user biasa.
- menayangkan semua user => `GET` pada alamat `localhost:8080/users/all`
- login => `POST` pada alamat `localhost:8080/login`, lalu pilih `Body > raw > JSON` dan masukkan
    ```json
    {
        "username": "admin"
    }
    ```
    agar user dengan username `admin` bisa login. Login ditandai dengan token(tidak menggunakan session). Jika login berhasil, maka akan muncul response
    ```json
    {
        "username": "admin",
        "token": "lcdmygocqj"
    }
    ```
- menambahkan user => `POST` pada alamat `localhost:8080/users/add`, gunakan token yang muncul setelah berhasil login agar bisa menambahkan user baru. lalu pilih `Body > raw > JSON` dan masukkan
    ```json
    {
        "token": "lcdmygocqj",
        "username": "johnSmith",
        "firstname": "john",
        "lastname": "smith",
        "email": "johnSS@gmail.com",
        "role": "user"
    }
    ```
    jika berhasil, maka akan muncul response
    ```json
    {
        "message": "user added"
    }
    ```
#### Product
- generate product => `GET` pada alamat `localhost:8080/products/generate/20`, ini akan melakukan auto generate project sesuai jumlah yang diinginkan yaitu `20` product, product yang digenerate adalah hasil dari program faker data. Jika berhasil, maka akan muncul response berikut:
    ```json
    {
        "message": "success generate data product"
    }
    ```
- menayangkan seluruh product => `GET` pada alamat `localhost:8080/products/all`, akan menampilkan seluruh product.
#### Order
- harus [login](#users) terlebih dulu dan mendapatkan token sebelum bisa membuat order
- untuk membuat order, `POST` pada alamat `localhost:8080/orders/add`, lalu pilih `Body > raw > JSON` dan masukkan
    ```json
    {
        "token": "lcdmygocqj",
        "userId": "63c8d7222b2eac2ed0d73aac",
        "productId": "63c8e3e32b2eac2ed0d73ab0",
        "amount": 5,
        "status": "pending"
    }
    ```
    jika berhasil, akan muncul response
    ```json
    {
        "message": "order created"
    }
    ```
- untuk melihat list order dari user, gunakan `GET` pada alamat `localhost:8080/orders/63c8d7222b2eac2ed0d73aac` dengan `63c8d7222b2eac2ed0d73aac` adalah `id` dari user yang membuat order.
- untuk menghapus order, gunakan `DELETE` pada alamat `localhost:8080/orders/63c8e78b2b2eac2ed0d73ac8` dengan `63c8e78b2b2eac2ed0d73ac8` adalah `id` dari order yang dibuat. Jika berhasil, maka akan muncul response
    ```json
    {
        "message": "success delete order",
        "deletedOrder": "63c8e78b2b2eac2ed0d73ac8"
    }
    ```