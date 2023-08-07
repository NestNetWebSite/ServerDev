package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThumbNailDto {

    private Long postId;                                        // 썸네일이 해당되는 게시물
    private String saveFileName;                                // 실제 저장된 파일 이름
    private String saveFilePath;                                // 파일 경로 (서버)
}
