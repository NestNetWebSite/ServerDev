package NestNet.NestNetWebSite.repository.like;

import NestNet.NestNetWebSite.domain.like.PostLike;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // 게시물로 조회
    List<PostLike> findAllByPost(Post post);

    // 회원, 게시물로 단건 조회 (해당 회원이 게시물에 좋아요를 눌렀는지 확인하기 위함)
    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    // 삭제
    @Modifying
    void delete(PostLike postLike);

    // 여러개 삭제
    @Modifying
    void deleteAllByPost(Post post);

}
