package NestNet.NestNetWebSite.controller.manager;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.MemberChangeAuthorityRequest;
import NestNet.NestNetWebSite.dto.request.MemberSignUpManagementRequest;
import NestNet.NestNetWebSite.dto.response.manager.MemberInfoResponse;
import NestNet.NestNetWebSite.dto.response.manager.MemberSignUpManagementResponse;
import NestNet.NestNetWebSite.service.manager.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "관리자 API")
public class ManagerController {

    private final ManagerService managerService;

    /*
    회원가입 요청 조회
     */
    @GetMapping("/manager/signup-request")
    @Operation(summary = "회원가입 요청 다건 조회", description = "모든 회원가입 요청을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 요청 조회 성공", content = @Content(schema = @Schema(implementation = MemberSignUpManagementResponse.class)))
    })
    public ApiResult<?> showSignUpRequests(){

        return managerService.findAllRequests();
    }

    /*
    회원가입 승인
     */
    @PostMapping("/manager/approve-signup")
    @Operation(summary = "회원가입 요청 단건 승인", description = "하나의 회원가입 요청을 승인한다. 회원가입 요청을 찾을 수 없을 시 500 에러를 반환한다.")
    public ApiResult<?> approveSignUpMember(@Valid @RequestBody MemberSignUpManagementRequest dto){

        return managerService.approveSignUp(dto);
    }

    /*
    회원 권한 변경
     */
    @PostMapping("/manager/change-authority")
    @Operation(summary = "회원 권한 단건 변경", description = "한명의 회원의 권한을 변경한다.")
    public ApiResult<?> changeMemberAuthority(@Valid @RequestBody MemberChangeAuthorityRequest dto){

        return managerService.changeAuthority(dto.getId(), dto.getMemberAuthority());
    }

    /*
    전체 회원 정보 조회
     */
    @GetMapping("/manager/member-info")
    @Operation(summary = "모든 회원 정보 조회", description = "회원 정보를 조회한다. 이때, 이름, 권한을 필터링(필수 아님)할 수 있다.", responses = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공", content = @Content(schema = @Schema(implementation = MemberInfoResponse.class)))
    })
    public ApiResult<?> showMemberInfo(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "memberAuthority", required = false) MemberAuthority memberAuthority){

        return managerService.findAllMemberInfo(name, memberAuthority);
    }

    /*
    회원 탈퇴
     */
    @GetMapping("/manager/member-withdraw")
    @Operation(summary = "회원 탈퇴", description = "관리자가 회원을 탈퇴처리한다.")
    public ApiResult<?> withdrawMember(@RequestParam(value = "member-id") Long id){

        return managerService.withDrawMember(id);
    }
}
