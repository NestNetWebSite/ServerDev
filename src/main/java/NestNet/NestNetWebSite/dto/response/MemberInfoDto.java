package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {

    private MemberAuthority memberAuthority;
    private String name;
    private String loginId;
    private String emailAddress;
    private String studentId;
    private int grade;
    private int graduateYear;
}
