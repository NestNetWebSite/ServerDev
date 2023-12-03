package NestNet.NestNetWebSite.repository.attendance;

import NestNet.NestNetWebSite.domain.attendance.Attendance;
import NestNet.NestNetWebSite.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    //당일 회원 출석 여부 조회
    @Query("select a from Attendance a where a.member =: member and a.time  >=: startOfDay and a.time <=: endOfDay")
    Optional<Attendance> findByMemberAndDay(@Param("member") Member member, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    // 주간 출석 통계 조회 (월~일)
    @Query("select a.member, count(a) from Attendance a where a.time  >=: startOfWeek and a.time <=: endOfWeek group by a.member")
    List<Object[]> findWeeklyCount(@Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);

    // 월간 출석 통계 조회
    @Query("select a.member, count(a) from Attendance a where a.time  >=: startDayOfMonth and a.time <=: endDayOfMonth group by a.member")
    List<Object[]> findMonthlyCount(@Param("startDayOfMonth") LocalDateTime startOfWeek, @Param("endDayOfMonth") LocalDateTime endOfWeek);

}
