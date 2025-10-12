# Social Media App

Java 17 + Spring Boot 3 + PostgreSQL ile geliştirilmiş basit bir sosyal medya servisidir.  
Spring Security kullanılmadan, Bcrypt ve hasher kullanılarak güvenlik sağlanmış olup ; token tabanlı 'custom' kimlik doğrulama middleware’i kullanılarak geliştirilmiştir.

---

## İçindekiler
- Özellikler
- Teknolojiler
- Kurulum/Çalıştırma Adımları
- Gerekli yapılandırma örnekleri
- Hazır ADMIN kullanıcının bilgisi ve/veya oluşturulma şekli
- Uç noktaların açıklamaları ve örnek akış (metinsel)
- Varsayımlar & kısıtlar

---

## Özellikler
- **Custom Token Auth**: *Login* → token üretimi, DB’de aktif saklama; *logout* → token sonlandırma.
- **TTL**: Token’ların geçerlilik süresi vardır (örn. `3600s`).
- **Roller**: `ADMIN` ve `USER`. Admin kullanıcı başlangıçta otomatik oluşturulur.
- **Kullanıcı İşlemleri**: Profil görüntüleme, şifre değiştirme, kendini silme, post/comment oluşturma/güncelleme; admin başka kullanıcıyı silebilir.
- **İçerik**: Post (imageUrl + caption(post açıklaması)), Comment, Like.
- **Yetkiler**: Post/Comment üzerinde sahiplik kontrolü; admin her şeyi güncelleyip/silebilir.
- **Sayaçlar**: Post görüntülenme ve beğeni sayısı tutulur.
- **Tutarlı Hata Formatı**: `docs/errors.md`’de belgeli.

---

## Teknolojiler
- **Java 17+**, **Spring Boot 3+**
- **PostgreSQL**
- **JPA/Hibernate**
- **Maven**
- **Docker & Docker Compose**
- **Postman** (koleksiyon + environment)

---

## Kurulum/Çalıştırma Adımları

> Docker Desktop çalışır durumda olmalı.
> Proje dizininde gerekli docker dosyaları zaten mevcuttur.
> Projenin ana(kök) dizininden aşağıdaki komutları takip ederek kurulum ve çalıştırmayı yapabilirsiniz.

Terminalden --> docker compose up -d --build
Uygulama: http://localhost:8080
DB: localhost:5432 (socialdb / social / socialpwd)
pgAdmin: http://localhost:5050 (admin@example.com / admin123)

#program durdurulmak istenirse 
Terminalden --> docker compose down
#eğer tüm cache' i ve veri tabanındaki verileri tamamen silmek isterseniz --> docker compose down -v

---

## **Gerekli Yapılandırma Örnekleri**

>application.properties  (dosya içeriği)

---
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/socialdb}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:social}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:socialpwd}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
app.token.ttl-seconds=3600
---

---

## **Hazır ADMIN kullanıcının bilgisi ve/veya oluşturulma şekli**

Uygulama ayağa kalktığında bootstrap/AdminSeeder.java şu kullanıcıyı yoksa oluşturur:

username: admin
password: admin123
role: ADMIN

---

## **Uç noktaların açıklamaları ve örnek akış (metinsel)**

(Body' ler postman' de raw ve formatı JSON olarak seçilmelidir.)

Authorization: API key  
               Key: Access-Token
               Value: {{accessToken}}
               Add To: Header

**AUTH**
-POST /api/auth/signup – Yeni kullanıcı oluşturur (rol: USER). Body: {username,password}

-POST /api/auth/login – Giriş + süreli erişim token üretir. Body: {username,password}

-POST /api/auth/logout – Geçerli token’ı sonlandırır. Header: Access-Token

-GET /api/auth/me – Aktif kullanıcının bilgisi. Header: Access-Token

**USERS**
-GET /api/users/{id} – Tekil kullanıcı profili. Header: Access-Token

-PUT /api/users/me/password – Kendi şifresini günceller. Header: Access-Token Body: {currentPassword,newPassword}

-DELETE /api/users/me – Kendi hesabını siler. Header: Access-Token

-DELETE /api/admin/users/{id} – ADMIN herhangi bir kullanıcıyı siler. Header: Access-Token (ADMIN)

**POSTS**
-POST /api/posts – Post oluşturur (resim + açıklama). Header: X-Access-Token Body: {imageUrl,caption}

-GET /api/posts/{id} – Post detay (yazar, sayaçlar, yorumlar). Header: Access-Token

-PUT /api/posts/{id} – Postu günceller (sahip veya ADMIN). Header: Access-Token Body: {imageUrl?,caption?}

-DELETE /api/posts/{id} – Postu siler (sahip veya ADMIN). Header: Access-Token

-POST /api/posts/{id}/view – Görüntülenme sayacını +1. Header: Access-Token

-GET /api/posts – Tüm postları listeler. Header: Access-Token

**COMMENTS**
-POST /api/posts/{id}/comments – Post’a yorum ekler. Header: Access-Token Body: {text}

-GET /api/posts/{id}/comments – Post’un yorumlarını listeler. Header: Access-Token

-DELETE /api/comments/{id} – Yorumu yorum sahibi, post sahibi veya ADMIN siler. Header: Access-Token

**LIKES**
-POST /api/posts/{id}/likes – Postu beğenir (kullanıcı başına tek). Header: Access-Token

-DELETE /api/posts/{id}/likes – Beğeniyi geri alır. Header: Access-Token

Not: Korumalı uç noktalar için her zaman Access-Token: <token> header’ını gönder.




