package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UnifiedPostResponse {

    private Long id;
    private String title;
    private String bodyContent;
    private Long viewCount;
    private int likeCount;
    private UnifiedPostType unifiedPostType;
    private String userName;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    boolean isMemberWritten;
}
