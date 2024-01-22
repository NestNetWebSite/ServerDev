package NestNet.NestNetWebSite.dto.response.noticepost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NoticePostListDto {

    private Long id;
    private String title;
    private LocalDateTime createdTime;
    private Long viewCount;
    private int likeCount;
    private String username;
}
