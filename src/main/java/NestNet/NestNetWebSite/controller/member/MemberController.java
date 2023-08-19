package NestNet.NestNetWebSite.controller.member;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.MemberFindIdRequestDto;
import NestNet.NestNetWebSite.dto.request.MemberGetTemporaryPwRequestDto;
import NestNet.NestNetWebSite.dto.request.MemberModifyInfoRequestDto;
import NestNet.NestNetWebSite.dto.request.MemberPasswordChangeRequestDto;
import NestNet.NestNetWebSite.service.mail.MailService;
import NestNet.NestNetWebSite.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
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
    public ApiResult<?> modifyMemberInfo(@Valid @RequestBody MemberModifyInfoRequestDto memberModifyInfoRequestDto,
                                         @AuthenticationPrincipal UserDetails userDetails){

        return memberService.modifyMemberInfo(memberModifyInfoRequestDto, userDetails.getUsername());
    }

    /*
    회원 이름 + 이메일로 아이디 찾기 -> 이메일로 아이디 전송
     */
    @PostMapping("/member/find-id")
    public ApiResult<?> findMemberId(@Valid @RequestBody MemberFindIdRequestDto memberFindIdRequestDto){

        String memberLoginId = memberService.findMemberId(memberFindIdRequestDto.getName(), memberFindIdRequestDto.getEmailAddress());

        if(memberLoginId == null){
            return ApiResult.error(HttpStatus.NOT_FOUND, "일치하는 회원이 없습니다.");
        }

        return mailService.sendEmailLoginId(memberFindIdRequestDto.getEmailAddress(), memberLoginId);
    }

    /*
    회원 아이디로 임시비밀번호 발급받기 -> 이메일로 임시 비밀번호 전송
    */
    @PostMapping("/member/get-temp-pw")
    public ApiResult<?> getTemporaryPassword(@Valid @RequestBody MemberGetTemporaryPwRequestDto dto){

        Map<String, String> emailAndPw = memberService.createTemporaryPassword(dto.getLoginId());

        memberService.changeMemberPassword(emailAndPw.get("tempPassword"), dto.getLoginId());

        return mailService.sendEmailTemporaryPassword(emailAndPw.get("email"), emailAndPw.get("tempPassword"));
    }

    /*
    회원 비밀번호 변경
     */
    @PostMapping("/member/change-pw")
    public ApiResult<?> changePassword(@Valid @RequestBody MemberPasswordChangeRequestDto dto,
                                       @AuthenticationPrincipal UserDetails userDetails){

        return memberService.changeMemberPassword(dto.getPassword(), userDetails.getUsername());
    }



}
