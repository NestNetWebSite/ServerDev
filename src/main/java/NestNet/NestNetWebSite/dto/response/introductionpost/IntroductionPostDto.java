package NestNet.NestNetWebSite.dto.response.introductionpost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class IntroductionPostDto {

    private Long id;
    private String title;
    private String bodyContent;
    private Long viewCount;
    private int likeCount;
    private String username;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    boolean isMemberWritten;
}
