package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 족보 게시물 하나에 들어가는 데이터
 */
@Getter
@AllArgsConstructor
@Builder
public class ExamCollectionPostResponse {

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
    private String userName;
}
