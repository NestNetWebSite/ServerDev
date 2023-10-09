package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class ExamCollectionPostModifyRequest {

    private Long id;
    private String title;
    private String bodyContent;
    private String subject;
    private String professor;
    private int year;
    private int semester;
    private ExamType examType;
}
