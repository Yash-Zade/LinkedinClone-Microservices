package com.yash.linkedin.post_service.controller;

import com.yash.linkedin.post_service.auth.UserContextHolder;
import com.yash.linkedin.post_service.client.MediaClient;
import com.yash.linkedin.post_service.dto.PostCreationDTO;
import com.yash.linkedin.post_service.dto.PostDTO;
import com.yash.linkedin.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/core")
public class PostController {

    private final PostService postService;
    private final MediaClient mediaClient;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostCreationDTO postDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        PostDTO createdPost = postService.createPost(postDto, userId);
        createdPost.setMediaUrl(mediaClient.uploadFile(postDto.getFile(), createdPost.getId()));
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        PostDTO postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDTO>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostDTO> posts = postService.getAllPosts(userId);
        return ResponseEntity.ok(posts);
    }
}

