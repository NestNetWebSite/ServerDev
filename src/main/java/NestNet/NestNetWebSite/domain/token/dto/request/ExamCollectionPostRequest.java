package NestNet.NestNetWebSite.domain.token.dto.request;

import NestNet.NestNetWebSite.domain.post.PostCategory;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExamCollectionPostRequest {

    private String title;
    private String bodyContent;
    private PostCategory postCategory;
    private String subject;
    private String professor;
    private int year;
    private int semester;
    private ExamType examType;


    //== DTO ---> Entity ==//
    public ExamCollectionPost toEntity(Member member){

        return ExamCollectionPost.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0L)
                .recommendationCount(0)
                .createdTime(LocalDateTime.now())
                .subject(this.subject)
                .professor(this.professor)
                .year(this.year)
                .semester(this.semester)
                .examType(this.examType)
                .build();
    }
}
