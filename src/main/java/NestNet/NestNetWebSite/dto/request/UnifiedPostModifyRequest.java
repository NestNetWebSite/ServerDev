package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import lombok.Getter;

@Getter
public class UnifiedPostModifyRequest {

    private Long id;
    private String title;
    private String bodyContent;
    private UnifiedPostType unifiedPostType;
}
