package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.post.introduction.IntroductionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IntroductionPostRepository extends JpaRepository<IntroductionPost, Long> {

    // Id(PK)로 단건 조회
    Optional<IntroductionPost> findById(Long id);

    // 자기소개 게시물 페이징 조회
    @Query("select distinct ip from IntroductionPost ip join fetch ip.attachedFileList")
    Page<IntroductionPost> findPostList(Pageable pageable);
}
