package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ExamCollectionPostListDto {

    private Long id;
    private String subject;
    private String professsor;
    private int year;
    private int semester;
    private ExamType examType;
}
