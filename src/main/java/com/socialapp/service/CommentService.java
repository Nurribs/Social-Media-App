package com.socialapp.service;

import com.socialapp.dto.CommentCreateReq;
import com.socialapp.dto.CommentResponse;
import com.socialapp.model.entity.Comment;
import com.socialapp.model.entity.Post;
import com.socialapp.model.entity.User;
import com.socialapp.repository.CommentRepository;
import com.socialapp.repository.PostRepository;
import com.socialapp.security.RoleHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository comments;
    private final PostRepository posts;
    private final RoleHelper roles;

    public CommentService(CommentRepository comments, PostRepository posts, RoleHelper roles) {
        this.comments = comments;
        this.posts = posts;
        this.roles = roles;
    }

    @Transactional
    public CommentResponse add(User author, Long postId, CommentCreateReq req) {
        Post p = posts.findById(postId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post ya da buna ait yorum mevcut değil."));
        Comment c = Comment.builder().post(p).author(author).text(req.text()).build();
        comments.save(c);
        return new CommentResponse(c.getId(), author.getId(), author.getUsername(), c.getText(), c.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> list(Long postId) {
        posts.findById(postId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post ya da buna ait yorum mevcut değil."));
        return comments.findByPostIdOrderByCreatedAtAsc(postId).stream().map(c ->
                new CommentResponse(c.getId(), c.getAuthor().getId(), c.getAuthor().getUsername(), c.getText(), c.getCreatedAt())
        ).toList();
    }

    @Transactional
    public void delete(User actor, Long commentId) {
        Comment c = comments.findById(commentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post ya da buna ait yorum mevcut değil"));
        boolean ownerOfComment = c.getAuthor().getId().equals(actor.getId());
        boolean ownerOfPost    = c.getPost().getAuthor().getId().equals(actor.getId());
        if (!(ownerOfComment || ownerOfPost || roles.isAdmin(actor))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Başka bir kullanıcnın post yorumunu silme yetkiniz yoktur.");
        }
        comments.delete(c);
    }
}
