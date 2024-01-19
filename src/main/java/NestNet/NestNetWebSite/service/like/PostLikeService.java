package NestNet.NestNetWebSite.service.like;

import NestNet.NestNetWebSite.domain.like.PostLike;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.like.PostLikeRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.PostRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /*
    좋아요 저장
     */
    @Transactional
    public void saveLike(Post post, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        PostLike postLike = new PostLike(post, member);

        postLikeRepository.save(postLike);
    }

    /*
    좋아요 취소
     */
    @Transactional
    public void cancelLike(Post post, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        Optional<PostLike> postLike = postLikeRepository.findByMemberAndPost(member, post);

        if(postLike.isPresent()){
            postLikeRepository.delete(postLike.get());
        }
    }

    /*
    로그인한 회원의 좋아요 여부 조회
     */
    public boolean isMemberLikedByPost(Post post, Member member){

        Optional<PostLike> like = postLikeRepository.findByMemberAndPost(member, post);

        if(like.isPresent()) return true;

        return false;
    }

    /*
    게시물 관련 좋아요 삭제
     */
    @Transactional
    public void deleteLike(Post post){

        postLikeRepository.deleteAllByPost(post);
    }
}
