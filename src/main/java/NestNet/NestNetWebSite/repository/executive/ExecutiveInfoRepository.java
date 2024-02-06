package NestNet.NestNetWebSite.repository.executive;


import NestNet.NestNetWebSite.domain.executive.ExecutiveInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExecutiveInfoRepository extends JpaRepository<ExecutiveInfo, Long> {

    // 전 임원 정보 조회
    @Query("select e from ExecutiveInfo e where e.year < FUNCTION('DATE_FORMAT', current_date, '%Y')")
    List<ExecutiveInfo> findPrevExecutiveInfo();

    // 현 임원 정보 조회
    @Query("select e from ExecutiveInfo e where e.year = FUNCTION('DATE_FORMAT', current_date, '%Y')")
    List<ExecutiveInfo> findCurrentExecutiveInfo();

}
