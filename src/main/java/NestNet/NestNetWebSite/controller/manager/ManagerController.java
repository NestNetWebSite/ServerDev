package NestNet.NestNetWebSite.controller.manager;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.MemberChangeAuthorityRequest;
import NestNet.NestNetWebSite.dto.request.MemberSignUpManagementRequest;
import NestNet.NestNetWebSite.service.manager.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    /*
    회원가입 요청 조회
     */
    @GetMapping("/manager/signup-request")
    public ApiResult<?> showSignUpRequests(){

        return managerService.findAllRequests();
    }

    /*
    회원가입 승인
     */
    @PostMapping("/manager/approve-signup")
    public ApiResult<?> approveSignUpMember(@Valid @RequestBody MemberSignUpManagementRequest dto){
        return managerService.approveSignUp(dto);
    }

    /*
    회원 권한 변경
     */
    @PostMapping("/manager/change-authority")
    public ApiResult<?> changeMemberAuthority(@Valid @RequestBody MemberChangeAuthorityRequest dto){

        return managerService.changeAuthority(dto.getId(), dto.getMemberAuthority());
    }

    /*
    회원 정보 조회
     */
    @GetMapping("/manager/member-info")
    public ApiResult<?> showMemberInfo(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "memberAuthority", required = false) MemberAuthority memberAuthority){

        return managerService.findAllMemberInfo(name, memberAuthority);
    }

    /*
    회원 탈퇴
     */
    @GetMapping("/manager/member-withdraw")
    public ApiResult<?> withdrawMember(@RequestParam(value = "member-id") Long id){

        return managerService.withDrawMember(id);
    }
}
