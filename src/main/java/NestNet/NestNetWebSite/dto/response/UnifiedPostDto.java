package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.PostCategory;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UnifiedPostDto {

    private Long id;
    private String title;
    private String bodyContent;
    private Long viewCount;
    private int likeCount;
    private UnifiedPostType unifiedPostType;
    private String userName;
}
