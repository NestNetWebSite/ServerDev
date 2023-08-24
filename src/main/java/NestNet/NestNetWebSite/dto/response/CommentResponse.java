package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponse {

    Long id;
    String username;
    String content;
    LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    boolean isMemberWritten;
}
