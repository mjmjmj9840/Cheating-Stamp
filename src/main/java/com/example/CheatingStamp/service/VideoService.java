package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.VideoRequestDto;
import com.example.CheatingStamp.model.Video;
import com.example.CheatingStamp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public Video savePost(VideoRequestDto requestDto) {
        Video video = requestDto.toEntity();
        videoRepository.save(video);
        return video;
    }

    public HashMap getVideoInfo(Long videoId) {
        Optional<Video> video = videoRepository.findById(videoId);
        HashMap<String, String> infoMap = new HashMap<String, String>();

        if (video.isPresent()) {
            infoMap.put("filePath", video.get().getFilePath());
            infoMap.put("username", video.get().getUsername());
            infoMap.put("examTitle", video.get().getExam().getTitle());
            infoMap.put("examId", video.get().getExam().getId().toString()); // answerService 전달용
        }

        return infoMap;
    }

    public void deleteVideo(Long videoId) {
        videoRepository.deleteById(videoId);
    }
}
