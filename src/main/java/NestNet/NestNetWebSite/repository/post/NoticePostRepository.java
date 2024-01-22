package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.post.notice.NoticePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NoticePostRepository extends JpaRepository<NoticePost, Long> {

    // Id(PK)로 단건 조회
    Optional<NoticePost> findById(Long id);

    // 공지사항 게시물 페이징 조회
    @Query("select np from NoticePost np join fetch np.member")
    Page<NoticePost> findAll(Pageable pageable);
}
