package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private boolean isMemberWritten;
}
