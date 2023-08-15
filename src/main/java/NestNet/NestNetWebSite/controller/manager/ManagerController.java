package NestNet.NestNetWebSite.controller.manager;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.MemberChangeAuthorityRequestDto;
import NestNet.NestNetWebSite.dto.request.MemberSignUpManagementRequestDto;
import NestNet.NestNetWebSite.dto.response.MemberInfoDto;
import NestNet.NestNetWebSite.service.manager.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/manager/approve-signup")
    public ApiResult<?> approveSignUpMember(@Valid @RequestBody MemberSignUpManagementRequestDto dto){
        return managerService.approveSignUp(dto);
    }

    @PostMapping("/manager/change-authority")
    public ApiResult<?> changeMemberAuthority(@Valid @RequestBody MemberChangeAuthorityRequestDto dto){

        return managerService.changeAuthority(dto.getId(), dto.getMemberAuthority());
    }

    @GetMapping("/manager/member-info")
    public ResponseEntity<List<MemberInfoDto>> showMemberInfo(){

        List<MemberInfoDto> result = managerService.findAllMemberInfo();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
