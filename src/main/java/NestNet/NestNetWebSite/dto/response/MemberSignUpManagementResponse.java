package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberSignUpManagementResponse {

    private String name;
    private String studentId;
    private String loginId;
    private MemberAuthority memberAuthority;
}
