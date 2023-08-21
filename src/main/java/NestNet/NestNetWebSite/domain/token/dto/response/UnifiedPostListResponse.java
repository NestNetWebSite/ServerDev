package NestNet.NestNetWebSite.domain.token.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UnifiedPostListResponse {

    private String username;
    private String title;
    private LocalDateTime createdTime;
    private Long viewCount;
    private int likeCount;
}
