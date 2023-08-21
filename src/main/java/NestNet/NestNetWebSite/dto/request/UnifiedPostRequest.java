package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPost;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedPostRequest {

    private String title;
    private String bodyContent;
    private UnifiedPostType unifiedPostType;                    // 게시판 소분류 (자유, 개발, 진로)

    //== DTO ---> Entity ==//
    public UnifiedPost toEntity(Member member){

        return UnifiedPost.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0L)
                .likeCount(0)
                .createdTime(LocalDateTime.now())
                .unifiedPostType(this.unifiedPostType)
                .build();
    }
}
