package NestNet.NestNetWebSite.domain.token.dto.response;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {

    private MemberAuthority memberAuthority;
    private String name;
    private String loginId;
    private String emailAddress;
    private String studentId;
    private int grade;
    private int graduateYear;
}
