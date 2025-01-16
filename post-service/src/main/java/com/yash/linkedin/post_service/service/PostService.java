package com.yash.linkedin.post_service.service;

import com.yash.linkedin.post_service.client.MediaClient;
import com.yash.linkedin.post_service.dto.PostCreationDTO;
import com.yash.linkedin.post_service.dto.PostDTO;
import com.yash.linkedin.post_service.entity.Post;
import com.yash.linkedin.post_service.event.PostCreatedEvent;
import com.yash.linkedin.post_service.exception.ResourceNotFoundException;
import com.yash.linkedin.post_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PostService {

    private final ModelMapper modelMapper;
    private final PostsRepository postsRepository;
    private final KafkaTemplate<Long, PostCreatedEvent> postCreatedEventKafkaTemplate;
    private final MediaClient mediaClient;

    public PostDTO createPost(PostCreationDTO postCreationDTO, Long userId){
        Post post = modelMapper.map(postCreationDTO,Post.class);
        post.setUserId(userId);
        Post savedPost = postsRepository.save(post);
        PostCreatedEvent postCreatedEvent =  PostCreatedEvent.builder()
                .creatorId(userId)
                .content(savedPost.getContent())
                .postId(savedPost.getId())
                .build();

        postCreatedEventKafkaTemplate.send("post-created-topic",postCreatedEvent);

        return modelMapper.map(postsRepository.save(savedPost), PostDTO.class);
    }

    public PostDTO getPostById(Long postId) {
        Post post =  postsRepository.getPostsById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+ postId));

        return modelMapper.map(post, PostDTO.class);
    }

    public List<PostDTO> getAllPosts(Long userId) {
        List<Post> posts = postsRepository.getPostsByUserId(userId);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .peek(postDTO -> postDTO.setMediaUrl(mediaClient.getMediaUrl(postDTO.getId())))
                .toList();
    }
}
