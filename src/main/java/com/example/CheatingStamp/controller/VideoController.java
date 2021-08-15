package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.dto.VideoRequestDto;
import com.example.CheatingStamp.security.UserDetailsImpl;
import com.example.CheatingStamp.service.S3Service;
import com.example.CheatingStamp.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/*
* /examEnd 컨트롤러 생성 후에 내용 이동
* */
@Controller
@RequiredArgsConstructor
public class VideoController {
    private final S3Service s3Service;
    private final VideoService videoService;

    @GetMapping("/upload")
    public String showForm() {
        return "TESTupload";
    }

    @PostMapping("/upload")
    public String uploadVideo(@AuthenticationPrincipal UserDetailsImpl userDetails, MultipartFile file) throws IOException {
        // s3에 응시 영상 업로드
        String filePath = s3Service.upload(file);
        // DB에 영상 이름과 url 저장
        VideoRequestDto requestDto = new VideoRequestDto();
        String username = userDetails.getUser().getUsername();
        requestDto.setTitle(username);
        requestDto.setFilePath(filePath);

        videoService.savePost(requestDto);

        return "redirect:/";
    }
}
