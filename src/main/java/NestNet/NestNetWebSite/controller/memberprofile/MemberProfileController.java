package NestNet.NestNetWebSite.controller.memberprofile;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.response.MemberProfileMemberInfoResponse;
import NestNet.NestNetWebSite.dto.response.memberprofile.PostInfoResponse;
import NestNet.NestNetWebSite.service.memberprofile.MemberProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원 프로필 API")
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    /*
    회원 정보 조회 (마이페이지)
     */
    @GetMapping("/member-profile/member-info")
    @Operation(summary = "회원 정보 조회", description = "특정 회원의 정보를 조회한다.", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = MemberProfileMemberInfoResponse.class)))
    })
    public ApiResult<?> showMemberInfo(@AuthenticationPrincipal UserDetails userDetails){

        return memberProfileService.findMemberInfoByLoginId(userDetails.getUsername(), false);
    }

    /*
    타 회원 정보 조회
     */
    @GetMapping("/member-profile/member-info/{member_login_id}")
    @Operation(summary = "회원 정보 조회", description = "특정 회원의 정보를 조회한다.", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = MemberProfileMemberInfoResponse.class)))
    })
    public ApiResult<?> showMemberInfo(@PathVariable("member_login_id") String memberLoginId){

        return memberProfileService.findMemberInfoByLoginId(memberLoginId, true);
    }

    /*
    회원이 작성한 글 모두 조회 --> 버튼을 숨길 수 있을 때.. 버튼을 숨길 수 없을 때는 현재 페이지의 주인과 로그인한 사용자가 일치하는지 확인해야함.
     */
    @GetMapping("/member-profile/my-post/{member_id}")
    @Operation(summary = "회원이 작성한 글 조회", description = "로그인한 사용자가 자신이 작성한 글을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = PostInfoResponse.class)))
    })
    public ApiResult<?> showMyPostList(@PathVariable("member_id") Long memberId,
                                       @AuthenticationPrincipal UserDetails userDetails){

        return memberProfileService.findAllPostById(memberId, userDetails.getUsername());
    }

    /*
    회원이 참여중인 스터디 조회
     */
    // 추후 업데이트 예정

}
