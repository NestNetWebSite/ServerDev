package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.introduction.IntroductionPost;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class IntroductionPostRequest {

    private String title;
    private String bodyContent;

    //== DTO ---> Entity ==//
    public IntroductionPost toEntity(Member member){

        return IntroductionPost.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0L)
                .recommendationCount(0)
                .createdTime(LocalDateTime.now())
                .build();
    }
}
