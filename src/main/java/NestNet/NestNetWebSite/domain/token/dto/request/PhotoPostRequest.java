package NestNet.NestNetWebSite.domain.token.dto.request;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PhotoPostRequest {

    private String title;
    private String bodyContent;

    //== DTO ---> Entity ==//
    public PhotoPost toEntity(Member member){

        return PhotoPost.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0L)
                .recommendationCount(0)
                .createdTime(LocalDateTime.now())
                .build();
    }

}
