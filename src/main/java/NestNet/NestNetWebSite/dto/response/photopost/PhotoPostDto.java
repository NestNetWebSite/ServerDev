package NestNet.NestNetWebSite.dto.response.photopost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PhotoPostDto {

    private Long id;
    private String title;
    private String bodyContent;
    private Long viewCount;
    private int likeCount;
    private String memberLoginId;
    private String username;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    boolean isMemberWritten;
}
