package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpManagementRequestDto {

    private String loginId;
    private MemberAuthority memberAuthority;
}
