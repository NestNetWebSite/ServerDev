package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.introduction.IntroductionPost;
import NestNet.NestNetWebSite.domain.post.notice.NoticePost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticePostRequest {

    private String title;
    private String bodyContent;

    //== DTO ---> Entity ==//
    public NoticePost toEntity(Member member){

        return NoticePost.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0L)
                .recommendationCount(0)
                .createdTime(LocalDateTime.now())
                .build();
    }
}
