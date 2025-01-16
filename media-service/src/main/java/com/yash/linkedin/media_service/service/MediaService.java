package com.yash.linkedin.media_service.service;


import com.yash.linkedin.media_service.entity.Media;
import com.yash.linkedin.media_service.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MediaService {
    private final MediaRepository mediaRepository;

    public void saveMedia(Media media) {
        mediaRepository.save(media);
    }

    public String getMediaByPostId(Long postId) {
        Media media = mediaRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Media not found for post id: " + postId));
        return media.getUrl();
    }
}
