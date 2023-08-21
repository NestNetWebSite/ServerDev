package NestNet.NestNetWebSite.domain.token.dto.request;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpManagementRequest {

    private String loginId;
    private MemberAuthority memberAuthority;
}
