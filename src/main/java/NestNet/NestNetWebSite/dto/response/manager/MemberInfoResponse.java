package NestNet.NestNetWebSite.dto.response.manager;

import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {

    List<MemberInfoDto> dtoList = new ArrayList<>();
}
