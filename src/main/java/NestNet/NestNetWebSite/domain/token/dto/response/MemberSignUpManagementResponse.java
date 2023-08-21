package NestNet.NestNetWebSite.domain.token.dto.response;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberSignUpManagementResponse {

    private String name;
    private String loginId;
    private String studentId;
    private int grade;
    private int graduateYear;
    private MemberAuthority memberAuthority;
}
