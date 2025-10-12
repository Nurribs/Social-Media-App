package com.socialapp.model.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User author;

    @Column(nullable = false, length = 600)
    private String imageUrl;

    @Column(nullable = false, length = 1000)
    private String caption;  //post açıklaması olacak

    @Builder.Default
    @Column(nullable = false)
    private long views = 0L;

    @Builder.Default
    @Column(nullable = false)
    private long likesCount = 0L;

    @Builder.Default
    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdAt = Instant.now();

    @Builder.Default
    @UpdateTimestamp
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

}
