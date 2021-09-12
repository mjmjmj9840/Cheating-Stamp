package com.example.CheatingStamp.dto;

import com.example.CheatingStamp.model.Video;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class VideoRequestDto {
    private Long id;
    private String username;
    private String filePath;

    public Video toEntity(){
        Video build = Video.builder()
                .id(id)
                .username(username)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public VideoRequestDto(Long id, String title, String filePath) {
        this.id = id;
        this.username = username;
        this.filePath = filePath;
    }
}
