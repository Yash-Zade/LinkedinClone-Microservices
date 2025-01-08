package com.yash.linkedin.post_service.repository;

import com.yash.linkedin.post_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {
  Optional<Post> getPostsById(Long postId);

  void deletePostById(Long postId);

  List<Post> getPostsByUserId(Long userId);
}