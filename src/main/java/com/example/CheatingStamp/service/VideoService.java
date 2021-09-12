package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.VideoRequestDto;
import com.example.CheatingStamp.model.Video;
import com.example.CheatingStamp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public Video savePost(VideoRequestDto requestDto) {
        Video video = requestDto.toEntity();
        videoRepository.save(video);
        return video;
    }
}
