package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long commentId;
    private String memberLoginId;
    private String username;
    private MemberAuthority memberAuthority;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private boolean isMemberWritten;
}
