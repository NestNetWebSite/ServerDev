package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberChangeAuthorityRequestDto {

    private Long id;
    private MemberAuthority memberAuthority;
}
