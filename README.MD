# Jejakin

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