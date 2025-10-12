##  Hata Yanıtları

**Standart format**
```json
{
  "timestamp": "2025-10-10T12:34:56.789+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/users/2",
  "message": "Token mevcut değil veya geçersiz."
}
```

**Sık karşılaşılan durumlar**

| Durum | HTTP | error | message (örnek) | Açıklama |
|---:|---:|---|---|---|
| Yetkisiz | 401 | Unauthorized | Token mevcut değil veya geçersiz. | Token yok/geçersiz/süresi dolmuş/logout edilmiş |
| Yasak | 403 | Forbidden | Yetkisiz kullanıcı. | Role/sahiplik yok |
| Bulunamadı | 404 | Not Found | Kullanıcı/Post/Comment Bulunamadı | Kaynak yok |
| Çakışma | 409 | Conflict | username_taken / already_liked | Benzersizlik/iş kuralı |
| Geçersiz veri | 400 | Bad Request | validation_failed | DTO/field hataları |
| Kötü kimlik bilgisi | 401 | Unauthorized | bad_credentials | Login hatalı |
| Sunucu hatası | 500 | Internal Server Error | (ops.) | Beklenmeyen hata |

**Örnekler**

401 – Token eksik:
```json
{
  "timestamp": "...",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/posts",
  "message": "Token mevcut değil veya geçersiz."
}
```

403 – Admin gerekli:
```json
{
  "timestamp": "...",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/admin/users/42",
  "message": "admin_required"
}
```

403 – Sahiplik yok:
```json
{
  "timestamp": "...",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/posts/10",
  "message": "Yetkisiz kullanıcı."
}
```

404 – Kaynak yok:
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "path": "/api/users/9999",
  "message": "Kullanıcı/Post/Comment Bulunamadı"
}
```

409 – username çakışması:
```json
{
  "timestamp": "...",
  "status": 409,
  "error": "Conflict",
  "path": "/api/auth/signup",
  "message": "username_taken"
}
```

400 – Validasyon hatası (alan bazlı):
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/users/me/password",
  "message": "validation_failed",
  "errors": [
    { "field": "currentPassword", "message": "must_not_be_blank" },
    { "field": "newPassword", "message": "too_short_min_6" }
  ]
}
```

**İstemci notları**
- Header adı: `Access-Token`.
- 401: yeniden `POST /api/auth/login` ile token alın.
- 403: admin/sahiplik kontrolü.

