package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.PostCategory;
import NestNet.NestNetWebSite.domain.post.PostType;
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
    private PostType postType;                    // 게시판 소분류 (자유, 개발, 진로)
}
