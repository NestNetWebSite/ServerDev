package NestNet.NestNetWebSite.service.like;

import NestNet.NestNetWebSite.domain.like.PostLike;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.repository.like.PostLikeRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    // Post 자식 객체를 가져오기 위한 간단한 로직 수행
    public Post findPost(Long postId){
        return entityManager.find(Post.class, postId);
    }

    /*
    좋아요 저장
     */
    @Transactional
    public void saveLike(Long postId, String memberLoginId){

        Post post = findPost(postId);
        Member member = memberRepository.findByLoginId(memberLoginId);
        PostLike postLike = new PostLike(post, member);

        postLikeRepository.save(postLike);
    }

    /*
    게시물 좋아요 수 조회
     */
    public Long findLikeCountByPost(Long postId){

        Post post = findPost(postId);
        return postLikeRepository.likeCount(post);
    }

    /*
    로그인한 회원의 좋아요 여부 조회
     */
    public boolean isMemberLikedByPost(Long postId, String memberLoginId){

        Post post = findPost(postId);
        Member member = memberRepository.findByLoginId(memberLoginId);
        return postLikeRepository.isMemberLiked(post, member);
    }

    /*
    좋아요 취소
     */
    @Transactional
    public void cancelLike(Long postId, String memberLoginId){

        Post post = findPost(postId);
        Member member = memberRepository.findByLoginId(memberLoginId);

        postLikeRepository.delete(post, member);
    }
}
