package com.socialapp.model.entity;

import java.time.Instant;
import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;

@Entity
@Table(name = "tokens", indexes = {
        @Index(name = "index_token_value", columnList = "token", unique = true),
        @Index(name = "index_token_user", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER) // laazy den eager a çevirdim çünkü proxy hatası alıyordum.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 200)
    private String token;

    @Column(name = "created_at",nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "expires_at",nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @PrePersist
    public void prePersist() {
        // eğer service set etmediyse garanti altına al
        if (createdAt == null) createdAt = Instant.now();
        if (expiresAt == null) expiresAt = Instant.now().plus(Duration.ofHours(12)); // süreyi istersen properties’ten oku
    }

    public boolean isActive(Instant now) {
        return revokedAt == null && now.isBefore(expiresAt);
    }
}
