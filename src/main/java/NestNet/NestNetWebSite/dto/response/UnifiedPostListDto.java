package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UnifiedPostListDto {

    private String username;
    private String title;
    private LocalDateTime createdTime;
    private Long viewCount;
    private int likeCount;
}
