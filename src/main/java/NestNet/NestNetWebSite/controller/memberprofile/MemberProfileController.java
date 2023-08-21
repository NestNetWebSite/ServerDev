package NestNet.NestNetWebSite.controller.memberprofile;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.service.memberprofile.MemberProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/member-profile")
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    /*
    회원 정보 조회
     */
    @GetMapping("/member-profile/member-info")
    public ApiResult<?> showMemberInfo(@AuthenticationPrincipal UserDetails userDetails){

        return memberProfileService.findMemberInfoById(userDetails.getUsername());
    }

    /*
    회원이 작성한 글 모두 조회 --> 버튼을 숨길 수 있을 때.. 버튼을 숨길 수 없을 때는 현재 페이지의 주인과 로그인한 사용자가 일치하는지 확인해야함.
     */
    @GetMapping("/member-profile/my-post")
    public ApiResult<?> showMyPostList(@AuthenticationPrincipal UserDetails userDetails){

        return memberProfileService.findAllPostById(userDetails.getUsername());
    }

    /*
    회원이 참여중인 스터디 조회
     */
    // 추후 업데이트 예정

}
