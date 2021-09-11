package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.VideoRequestDto;
import com.example.CheatingStamp.repository.VideoRepository;
import com.example.CheatingStamp.model.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public void savePost(VideoRequestDto requestDto) {
        videoRepository.save(requestDto.toEntity());
    }

    public HashMap getVideoInfo(Long videoId) {
        Optional<Video> video = videoRepository.findById(videoId);
        HashMap<String, String> infoMap = new HashMap<String, String>();

        if (video.isPresent()) {
            infoMap.put("filePath", video.get().getFilePath());
            infoMap.put("username", video.get().getTitle());
            infoMap.put("examTitle", video.get().getExam().getTitle());
        }

        return infoMap;
    }
}
