package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ExamCollectionPostDto {

    private Long id;
    private String title;
    private String bodyContent;
    private String subject;
    private String professsor;
    private int year;
    private int semester;
    private ExamType examType;
    private String userName;
}
