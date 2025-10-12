package com.socialapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "comments")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Post post;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User author;

    @Column(nullable = false, length = 1000)
    private String text;

    @CreationTimestamp
    private Instant createdAt;
}
