package com.yash.linkedin.post_service.service;

import com.yash.linkedin.post_service.entity.PostLike;
import com.yash.linkedin.post_service.exception.BadRequestException;
import com.yash.linkedin.post_service.exception.ResourceNotFoundException;
import com.yash.linkedin.post_service.repository.PostLikeRepository;
import com.yash.linkedin.post_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;

    public void likePost(Long postId, Long userId) {
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: "+postId);

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(alreadyLiked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
    }

    public void unlikePost(Long postId, Long userId) {
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: "+postId);
        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!alreadyLiked) throw new BadRequestException("Cannot unlike the post which is not liked.");
        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
