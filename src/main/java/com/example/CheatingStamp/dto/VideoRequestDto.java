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
    private boolean isMobile;

    public Video toEntity(){
        Video build = Video.builder()
                .id(id)
                .username(username)
                .filePath(filePath)
                .isMobile(isMobile)
                .build();
        return build;
    }

    @Builder
    public VideoRequestDto(Long id, String username, String filePath, boolean isMobile) {
        this.id = id;
        this.username = username;
        this.filePath = filePath;
        this.isMobile = isMobile;
    }
}
