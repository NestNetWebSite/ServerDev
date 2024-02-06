package NestNet.NestNetWebSite.repository.life4cut;

import NestNet.NestNetWebSite.domain.life4cut.Life4Cut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Life4CutRepository extends JpaRepository<Life4Cut, Long> {

    // id(PK)로 단건 조회
    Optional<Life4Cut> findById(Long id);

    // 여러 건 페이징 조회 (id 내림차순)
    Page<Life4Cut> findAll(Pageable pageable);

    // 여러 건 랜덤 조회
    @Query(value = "select * from Life4Cut order by rand() limit :size", nativeQuery = true)
    List<Life4Cut> findByRandom(@Param("size") int size);

    // 삭제
    @Modifying
    void delete(Life4Cut life4Cut);

}
