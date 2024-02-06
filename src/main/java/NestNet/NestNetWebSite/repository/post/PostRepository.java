package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // PK(id)로 조회
    Optional<Post> findById(Long postId);

    // 사용자가 쓴 글 조회 (족보, 통합, 사진 게시판)
    @Query("select p from Post p where p.member =:member")
    List<Post> findAllByMember (@Param("member") Member member);

    // 최근 게시물 목록 조회
    Page<Post> findAll(Pageable pageable);

    // 게시물 삭제
    @Modifying
    void delete(Post post);

}
