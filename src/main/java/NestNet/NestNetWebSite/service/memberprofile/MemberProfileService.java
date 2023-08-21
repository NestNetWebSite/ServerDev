package NestNet.NestNetWebSite.service.memberprofile;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.response.MemberProfileMemberInfoResponse;
import NestNet.NestNetWebSite.dto.response.PostTitleResponse;
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
    public ApiResult<?> findMemberInfoById(String loginId){

        Member member = memberRepository.findByLoginId(loginId);

        MemberProfileMemberInfoResponse memberInfoDto = new MemberProfileMemberInfoResponse(member.getLoginId(), member.getName(),
                member.getEmailAddress(), member.getMemberAuthority(), member.getGrade(), member.getGraduateYear());

        return ApiResult.success(memberInfoDto);
    }

    /*
    본인이 작성한 글 목록 조회
     */
    public ApiResult<?> findAllPostById(String loginId){

        Member member = memberRepository.findByLoginId(loginId);

        List<Post> postList = postRepository.findAllPostByMember(member);
        List<PostTitleResponse> result = new ArrayList<>();
        for(Post post : postList){
            result.add(new PostTitleResponse(post.getId(), post.getTitle(), post.getPostCategory()));
        }

        return ApiResult.success(result);
    }

}
