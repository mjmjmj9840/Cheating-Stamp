package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.VideoRequestDto;
import com.example.CheatingStamp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public void savePost(VideoRequestDto requestDto) {
        videoRepository.save(requestDto.toEntity());
    }
}
