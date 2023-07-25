package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.PostCategory;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UnifiedPostDto {

    private Long memberId;
    private String title;
    private String bodyContent;
    private PostCategory postCategory;
    private UnifiedPostType unifiedPostType;                    // 게시판 소분류 (자유, 개발, 진로)
}
