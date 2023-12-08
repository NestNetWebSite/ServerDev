package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.photo.ThumbNail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface ThumbNailRepository extends JpaRepository<ThumbNail, Long> {

    // Id(PK)로 단건 조회
    Optional<ThumbNail> findById(Long id);

    // 게시물로 단건 조회
    Optional<ThumbNail> findByPost(Post post);

    // 사진 게시판 썸네일 모두 조회 (페이징)
    Page<ThumbNail> findAll(Pageable pageable);

    @Modifying
    void delete(ThumbNail thumbNail);

}
