package NestNet.NestNetWebSite.dto.response.manager;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MemberSignUpManagementResponse {

    private List<MemberSignUpManagementDto> dtoList = new ArrayList<>();
}
