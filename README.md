# Social Media App

Java 17 + Spring Boot 3 + PostgreSQL ile geliştirilmiş basit bir sosyal medya servisidir.  
Spring Security kullanılmadan, Bcrypt ve hasher kullanılarak güvenlik sağlanmış olup ; token tabanlı 'custom' kimlik doğrulama middleware’i kullanılarak geliştirilmiştir.

---

## İçindekiler
- [Özellikler](#özellikler)
- [Teknolojiler](#teknolojiler)
- [Kurulum/Çalıştırma-Adımları](#kurulumçalıştırma-adımları)
- [Gerekli-Yapılandırma-Örnekleri](#gerekli-yapılandırma-örnekleri)
- [Hazır-ADMIN-Kullanıcısı](#hazır-admin-kullanıcısı)
- [Uç-Noktaların-Açıklamaları--Örnek-Akışlar](#uç-noktaların-açıklamaları--örnek-akışlar)
- [Varsayımlar--Kısıtlar](#varsayımlar--kısıtlar)

---

## Özellikler

- **Custom Token Auth**: Login → token üretimi, DB’de aktif saklama; logout → token sonlandırma.
- **TTL**: Token’ların geçerlilik süresi vardır (örn. `3600s`).
- **Roller**: `ADMIN` ve `USER`. Admin kullanıcı başlangıçta otomatik oluşturulur.
- **Kullanıcı İşlemleri**: Profil görüntüleme, şifre değiştirme, kendini silme, post/comment oluşturma/güncelleme; admin başka kullanıcıyı silebilir.
- **İçerik**: Post (imageUrl + caption), Comment, Like.
- **Yetkiler**: Post/Comment üzerinde sahiplik kontrolü; admin her şeyi güncelleyip/silebilir.
- **Sayaçlar**: Post görüntülenme ve beğeni sayısı tutulur.
- **Tutarlı Hata Formatı**: `docs/errors.md`’de belgeli.

---

## Teknolojiler

- Java 17+
- Spring Boot 3+
- PostgreSQL
- JPA / Hibernate
- Maven
- Docker & Docker Compose
- Postman (koleksiyon + environment)

---

## Kurulum/Çalıştırma Adımları

> Docker Desktop çalışır durumda olmalı.  
> Proje dizininde gerekli docker dosyaları zaten mevcuttur.

```bash
docker compose up -d --build
```
- Uygulama: http://localhost:8080  
- DB: localhost:5432 (socialdb / social / socialpwd)  
- pgAdmin: http://localhost:5050 (admin@example.com / admin123)

Durdurmak için:
```bash
docker compose down
```

Tüm verileri temizlemek için:
```bash
docker compose down -v
```

---

## Gerekli Yapılandırma Örnekleri

### application.properties
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/socialdb}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:social}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:socialpwd}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
app.token.ttl-seconds=3600
```

---

## Hazır ADMIN Kullanıcısı

Uygulama ayağa kalktığında `bootstrap/AdminSeeder.java` şu kullanıcıyı yoksa oluşturur:

- username: admin  
- password: admin123  
- role: ADMIN  

---

## Uç Noktaların Açıklamaları & Örnek Akışlar

Body’ler Postman'de `raw` + `JSON` olmalıdır.  
**Authorization:**
- Key: Access-Token  
- Value: `{{accessToken}}`  
- Add To: Header

### AUTH
- `POST /api/auth/signup` – Yeni kullanıcı oluşturur (rol: USER)  
- `POST /api/auth/login` – Giriş + token üretir  
- `POST /api/auth/logout` – Token sonlandırır  
- `GET /api/auth/me` – Oturum sahibi kullanıcıyı döner  

### USERS
- `GET /api/users/{id}` – Profil görüntüleme  
- `PUT /api/users/me/password` – Şifre değiştirme  
- `DELETE /api/users/me` – Kendi hesabını silme  
- `DELETE /api/admin/users/{id}` – Admin kullanıcı silme  

### POSTS
- `POST /api/posts` – Post oluşturma  
- `GET /api/posts/{id}` – Detaylı post bilgisi  
- `PUT /api/posts/{id}` – Güncelleme (sahip veya admin)  
- `DELETE /api/posts/{id}` – Silme (sahip veya admin)  
- `POST /api/posts/{id}/view` – Görüntülenme +1  
- `GET /api/posts` – Tüm postlar  

### COMMENTS
- `POST /api/posts/{id}/comments` – Yorum ekleme  
- `GET /api/posts/{id}/comments` – Yorumları listeleme  
- `DELETE /api/comments/{id}` – Yorumu silme (sahip/post sahibi/admin)  

### LIKES
- `POST /api/posts/{id}/likes` – Beğenme  
- `DELETE /api/posts/{id}/likes` – Beğeni kaldırma  

### Örnek Akışlar

#### 1. Temel Akış
```text
Signup → Login → Post Oluştur → View → Like → Comment → Logout
```

#### 2. Sahiplik / Yetki Kontrolü
- Kullanıcı A → B’nin postunu silemez  
- Admin veya post sahibi → silebilir  

#### 3. Admin Moderasyon
- Admin giriş yapar  
- İçerik siler veya kullanıcıyı kaldırır → tüm ilişkili veriler de silinir  

#### 4. Token Süresi Dolarsa
- `401 Unauthorized` hatası alınır  
- Tekrar login yapılır → yeni token kullanılır  

---

## Varsayımlar & Kısıtlar

### Varsayımlar
- Roller sadece `ADMIN` ve `USER`  
- Admin kullanıcı başlangıçta seed edilir  
- Spring Security yok, custom token mevcut  
- Token `Access-Token` header'ı ile gönderilir  
- Token TTL: 3600s  
- `username` alanı benzersizdir  
- `POST /posts/{id}/view` → sayaç artışı (non-idempotent)  
- Like işlemleri idempotent (tekrar beğeni hata vermez)  
- Yorum silme yetkisi: yorum sahibi, post sahibi veya admin  

### Kısıtlar / Kapsam Dışı
- Gerçek dosya yükleme yok, sadece `imageUrl`  
- Feed, takip, bildirim vb. gelişmiş özellikler yok  
- Rate limiting / brute-force koruması yok  
- Şifre kuralları örnek seviyede  
- Sayfalama, filtreleme gelişmiş değil  

### Veri & Kalıcılık
- PostgreSQL & Docker Compose  
- Cascade silmeler aktif (referans bütünlüğü korunur)  
- `createdAt`, `updatedAt` alanları mevcuttur  
- Sayaçlar servis sırasında güncellenir  

### Güvenlik
- UUID tabanlı token, DB’de tutulur  
- Refresh token yok  
- Çoklu oturumlara izin verilir  

### API Davranışı
- Tüm hatalar tutarlı JSON döner (`GlobalExceptionHandler`)  
- `POST /likes` ikinci kez çağrıldığında no-op  
- `DELETE /likes` yoksa da 204 döner  
- Request validation aktiftir (şifre değişikliği vs.)  

### Operasyonel
- `docker compose up -d --build` → tam kurulum  
- Yapılandırma `application.properties` içinde  
- Postman collection + environment → `postman/` klasöründe  

