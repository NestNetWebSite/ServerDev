package NestNet.NestNetWebSite.dto.response.examcollectionpost;

import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ExamCollectionPostDto {

    private Long id;
    private String title;
    private String bodyContent;
    private Long viewCount;
    private int likeCount;
    private String subject;
    private String professor;
    private int year;
    private int semester;
    private ExamType examType;
    private String memberLoginId;
    private String username;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    boolean isMemberWritten;
}
