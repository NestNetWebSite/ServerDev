package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberProfileMemberInfoResponse {

    private Long id;
    private String loginId;
    private String name;
    private String emailAddress;
    private String studentId;
    private MemberAuthority memberAuthority;
    private int grade;
    private int graduateYear;
    private boolean isLoginMember;
}
