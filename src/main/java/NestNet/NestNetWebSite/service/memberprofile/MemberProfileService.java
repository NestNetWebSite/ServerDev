package NestNet.NestNetWebSite.service.memberprofile;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.response.MemberProfileMemberInfoResponse;
import NestNet.NestNetWebSite.dto.response.memberprofile.PostInfoDto;
import NestNet.NestNetWebSite.dto.response.memberprofile.PostInfoResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /*
    회원 프로필 조회
     */
    public ApiResult<?> findMemberInfoByLoginId(String memberLoginId, boolean isOtherMember){

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        MemberProfileMemberInfoResponse memberInfoDto;

        if(!isOtherMember){
            memberInfoDto = new MemberProfileMemberInfoResponse(member.getId(), member.getLoginId(), member.getName(),
                    member.getEmailAddress(), member.getStudentId(), member.getMemberAuthority(), member.getGrade(), member.getGraduateYear(), true);
        }
        else{       //타 회원 정보이면
            memberInfoDto = new MemberProfileMemberInfoResponse(member.getId(), member.getLoginId(), member.getName(),
                    member.getEmailAddress(), member.getStudentId(), member.getMemberAuthority(), member.getGrade(), member.getGraduateYear(), false);
        }

        return ApiResult.success(memberInfoDto);
    }

    /*
    본인이 작성한 글 목록 조회
     */
    public ApiResult<?> findAllPostById(Long memberId, String loginId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

//        if(memberId != member.getId()) throw new CustomException(ErrorCode.MEMBER_NO_PERMISSION);

        List<Post> postList = postRepository.findAllByMember(member);

        List<PostInfoDto> dtoList = new ArrayList<>();
        for(Post post : postList){
            dtoList.add(new PostInfoDto(post.getId(), post.getTitle(), post.getPostCategory(), post.getViewCount(), post.getLikeCount()));
        }

        PostInfoResponse postInfoResponse = new PostInfoResponse(dtoList);

        return ApiResult.success(postInfoResponse);
    }

}
