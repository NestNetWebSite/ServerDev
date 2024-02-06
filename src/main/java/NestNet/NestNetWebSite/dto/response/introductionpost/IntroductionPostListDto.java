package NestNet.NestNetWebSite.dto.response.introductionpost;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class IntroductionPostListDto {

    private Long id;
    private String title;
    private Long viewCount;
    private int likeCount;
    private LocalDateTime createdTime;

    private String saveFilePath;        // 사진 경로
    private String saveFileName;        // 사진 이름
}
