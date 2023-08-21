package NestNet.NestNetWebSite.domain.token.dto.response;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberProfileMemberInfoResponse {

    private String loginId;
    private String name;
    private String emailAddress;
    private MemberAuthority memberAuthority;
    private int grade;
    private int graduateYear;

}
