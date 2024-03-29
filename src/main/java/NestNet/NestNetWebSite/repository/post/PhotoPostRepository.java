package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PhotoPostRepository extends JpaRepository<PhotoPost, Long> {

    // Id(PK)로 단건 조회
    Optional<PhotoPost> findById(Long id);

    // 사진 게시판 리스트 조회(페이징)
    @Query("select distinct pp from PhotoPost pp join fetch pp.attachedFileList")
    Page<PhotoPost> findAllThumbNail(Pageable pageable);

}
