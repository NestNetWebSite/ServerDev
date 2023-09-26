package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ThumbNailResponse {

    private Long postId;                                        // 썸네일이 해당되는 게시물
    private String title;                                       // 제목
    private Long viewCount;                                   // 좋아요 수
    private Integer likeCount;                                   // 조회 수
    private String saveFilePath;                                // 파일 경로 (서버)
    private String saveFileName;                                // 실제 저장된 파일 이름
}
