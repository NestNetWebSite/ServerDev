package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.post.unified.UnifiedPost;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UnifiedPostRepository extends JpaRepository<UnifiedPost, Long> {

    // Id(PK)로 단건 조회
    Optional<UnifiedPost> findById(Long id);

    // 통합 게시판 (자유 / 개발 / 진로) 목록 조회
    @Query("select u from UnifiedPost u where " +
            "(:unifiedPostType is null or u.unifiedPostType =: unifiedPostType) " +
            "order by u.id desc")
    Page<UnifiedPost> findByUnifiedPostTypeByPaging(@Param("unifiedPostType") UnifiedPostType unifiedPostType, Pageable pageable);

}
