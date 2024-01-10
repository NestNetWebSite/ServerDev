package NestNet.NestNetWebSite.repository.photofile;

import NestNet.NestNetWebSite.domain.photofile.PhotoFile;
import NestNet.NestNetWebSite.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhotoFileRepository extends JpaRepository<PhotoFile, Long> {

    // id(PK)로 단건 조회
    Optional<PhotoFile> findById(Long id);

    // 게시물에 해당하는 파일 모두 조회
    List<PhotoFile> findAllByPost(Post post);

    // 게시물 + 파일명으로 파일 단건 조회
    @Query("select f from PhotoFile f where f.post =:post and f.saveFileName =:saveFileName")
    Optional<PhotoFile> findByPostAndSaveFileName(@Param("post") Post post, @Param("saveFileName") String saveFileName);

    // 썸네일 조회 (페이징)
    @Query("select f from PhotoFile f where f.thumbNail = true " +
            "group by f.post")
    Page<PhotoFile> findThumbNail(Pageable pageable);

}
