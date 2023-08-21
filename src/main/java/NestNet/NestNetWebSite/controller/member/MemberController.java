package NestNet.NestNetWebSite.controller.member;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.token.dto.request.MemberFindIdRequest;
import NestNet.NestNetWebSite.domain.token.dto.request.MemberGetTemporaryPwRequest;
import NestNet.NestNetWebSite.domain.token.dto.request.MemberModifyInfoRequest;
import NestNet.NestNetWebSite.domain.token.dto.request.MemberPasswordChangeRequest;
import NestNet.NestNetWebSite.service.mail.MailService;
import NestNet.NestNetWebSite.service.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MailService mailService;
    private final MemberService memberService;

    /*
    회원 정보 변경
     */
    @PostMapping("/member/modify-info")
    public ApiResult<?> modifyMemberInfo(@Valid @RequestBody MemberModifyInfoRequest memberModifyInfoRequest,
                                         @AuthenticationPrincipal UserDetails userDetails){

        return memberService.modifyMemberInfo(memberModifyInfoRequest, userDetails.getUsername());
    }

    /*
    회원 이름 + 이메일로 아이디 찾기 -> 이메일로 아이디 전송
     */
    @PostMapping("/member/find-id")
    public ApiResult<?> findMemberId(@Valid @RequestBody MemberFindIdRequest memberFindIdRequest, HttpServletResponse response){

        String memberLoginId = memberService.findMemberId(memberFindIdRequest.getName(), memberFindIdRequest.getEmailAddress());

        if(memberLoginId == null){
            return ApiResult.error(response, HttpStatus.NOT_FOUND, "일치하는 회원이 없습니다.");
        }

        return mailService.sendEmailLoginId(response, memberFindIdRequest.getEmailAddress(), memberLoginId);
    }

    /*
    회원 아이디로 임시비밀번호 발급받기 -> 이메일로 임시 비밀번호 전송
    */
    @PostMapping("/member/get-temp-pw")
    public ApiResult<?> getTemporaryPassword(@Valid @RequestBody MemberGetTemporaryPwRequest dto, HttpServletResponse response){

        Map<String, String> emailAndPw = memberService.createTemporaryPassword(dto.getLoginId());

        memberService.changeMemberPassword(emailAndPw.get("tempPassword"), dto.getLoginId());

        return mailService.sendEmailTemporaryPassword(response, emailAndPw.get("email"), emailAndPw.get("tempPassword"));
    }

    /*
    회원 비밀번호 변경
     */
    @PostMapping("/member/change-pw")
    public ApiResult<?> changePassword(@Valid @RequestBody MemberPasswordChangeRequest dto,
                                       @AuthenticationPrincipal UserDetails userDetails){

        return memberService.changeMemberPassword(dto.getPassword(), userDetails.getUsername());
    }

    /*
    회원 탈퇴
     */
    @GetMapping("/member/withdraw")
    public ApiResult<?> withdrawMember(@AuthenticationPrincipal UserDetails userDetails){
        return memberService.withDrawMember(userDetails.getUsername());
    }

    /*
    테스트용
     */
    @GetMapping("/member/test")
    public ApiResult<?> test(){
        return ApiResult.success("테스트 성공");
    }


}
