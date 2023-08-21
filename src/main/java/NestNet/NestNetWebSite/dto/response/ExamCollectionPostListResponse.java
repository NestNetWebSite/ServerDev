package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 족보 리스트에 들어가는 데이터
 */
@Getter
@AllArgsConstructor
@Builder
public class ExamCollectionPostListResponse {

    private Long id;
    private String subject;
    private String professor;
    private int year;
    private int semester;
    private ExamType examType;
    private String userName;
}
