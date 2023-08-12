package NestNet.NestNetWebSite.service.memberprofile;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.response.MemberProfileMemberInfoDto;
import NestNet.NestNetWebSite.dto.response.PostTitleDto;
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
    public MemberProfileMemberInfoDto findMemberInfoById(Long id){

        Member member = memberRepository.findById(id);

        return new MemberProfileMemberInfoDto(member.getLoginId(), member.getName(),
                member.getEmailAddress(), member.getMemberAuthority(), member.getGrade(), member.getGraduateYear());
    }

    /*
    본인이 작성한 글 목록 조회
     */
    public List<PostTitleDto> findAllPostById(String loginId){

        Member member = memberRepository.findByLoginId(loginId);

        List<Post> postList = postRepository.findAllPostByMember(member);
        List<PostTitleDto> result = new ArrayList<>();
        for(Post post : postList){
            result.add(new PostTitleDto(post.getId(), post.getTitle(), post.getPostCategory()));
        }

        return result;
    }

}
