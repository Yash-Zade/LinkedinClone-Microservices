package com.yash.linkedin.post_service.controller;

import com.yash.linkedin.post_service.auth.UserContextHolder;
import com.yash.linkedin.post_service.auth.UserInterceptor;
import com.yash.linkedin.post_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/likes")
public class LikesController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        postLikeService.likePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        postLikeService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}
