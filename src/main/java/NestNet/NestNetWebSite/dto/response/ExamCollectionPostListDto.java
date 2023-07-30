package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 족보 리스트에 들어가는 데이터
 */
@Getter
@AllArgsConstructor
@Builder
public class ExamCollectionPostListDto {

    private Long id;
    private String subject;
    private String professor;
    private int year;
    private int semester;
    private ExamType examType;
    private String userName;
}
