package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.post.*;
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
public class UnifiedPostRequestDto {

    private Long memberId;
    private String title;
    private String bodyContent;
    private PostCategory postCategory;
    private UnifiedPostType unifiedPostType;                    // 게시판 소분류 (자유, 개발, 진로)


    //== DTO ---> Entity ==//
    public UnifiedPost toEntity(Member member){

        return UnifiedPost.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0)
                .recommendationCount(0)
                .postCategory(this.postCategory)
                .createdTime(LocalDateTime.now())
                .unifiedPostType(this.unifiedPostType)
                .build();
    }
}
