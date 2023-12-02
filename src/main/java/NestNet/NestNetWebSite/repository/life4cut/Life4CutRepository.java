package NestNet.NestNetWebSite.repository.life4cut;

import NestNet.NestNetWebSite.domain.life4cut.Life4Cut;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public interface Life4CutRepository extends JpaRepository<Life4Cut, Long> {

    // id(PK)로 단건 조회
    Optional<Life4Cut> findById(Long id);

    // 여러 건 페이징 조회 (id 내림차순)
    Page<Life4Cut> findBy(Pageable pageable);

    // 삭제
    @Modifying
    void delete(Life4Cut life4Cut);

}
