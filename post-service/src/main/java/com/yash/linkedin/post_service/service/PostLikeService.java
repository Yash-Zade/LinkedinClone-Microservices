package com.yash.linkedin.post_service.service;

import com.yash.linkedin.post_service.auth.UserContextHolder;
import com.yash.linkedin.post_service.entity.Post;
import com.yash.linkedin.post_service.entity.PostLike;
import com.yash.linkedin.post_service.event.PostLikedEvent;
import com.yash.linkedin.post_service.exception.BadRequestException;
import com.yash.linkedin.post_service.exception.ResourceNotFoundException;
import com.yash.linkedin.post_service.repository.PostLikeRepository;
import com.yash.linkedin.post_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;
    private final KafkaTemplate<Long, PostLikedEvent> postLikedEventKafkaTemplate;


    public void likePost(Long postId) {

        Long userId = UserContextHolder.getCurrentUserId();

        Post post = postsRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id: "+postId));

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(alreadyLiked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .likedByUserId(userId)
                .creatorId(post.getUserId()).build();

        postLikedEventKafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
    }

    public void unlikePost(Long postId, Long userId) {
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: "+postId);
        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!alreadyLiked) throw new BadRequestException("Cannot unlike the post which is not liked.");
        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
