package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.PostCategory;
import NestNet.NestNetWebSite.domain.post.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.ExamType;
import NestNet.NestNetWebSite.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExamCollectionPostRequestDto {

    private Long memberId;
    private String title;
    private String bodyContent;
    private PostCategory postCategory;
    private String subject;
    private String professsor;
    private int year;
    private int semester;
    private ExamType examType;


    //== DTO ---> Entity ==//
    public Post toEntity(Member member){

        return ExamCollectionPost.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0)
                .recommendationCount(0)
                .postCategory(this.postCategory)
                .createdTime(LocalDateTime.now())
                .subject(this.subject)
                .professsor(this.professsor)
                .year(this.year)
                .semester(this.semester)
                .examType(this.examType)
                .build();
    }
}
