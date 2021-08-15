package com.example.CheatingStamp.dto;

import com.example.CheatingStamp.model.Video;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class VideoRequestDto {
    private Long id;
    private String title;  // title에 업로드한 username 저장
    private String filePath;

    public Video toEntity(){
        Video build = Video.builder()
                .id(id)
                .title(title)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public VideoRequestDto(Long id, String title, String filePath) {
        this.id = id;
        this.title = title;
        this.filePath = filePath;
    }
}
