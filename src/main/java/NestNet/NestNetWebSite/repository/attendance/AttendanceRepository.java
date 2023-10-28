package NestNet.NestNetWebSite.repository.attendance;

import NestNet.NestNetWebSite.domain.attendance.Attendance;
import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AttendanceRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(Attendance attendance){
        entityManager.persist(attendance);
    }

    //=========================================조회=========================================//

    //당일 회원 출석 여부 조회
    public boolean isMemberAttended(Member member){

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);

        List<Attendance> result = entityManager.createQuery(
                        "select a from Attendance a where a.time  >=: startOfDay and a.time <=: endOfDay", Attendance.class)
                .setParameter("startOfDay", startOfDay)
                .setParameter("endOfDay", endOfDay)
                .getResultList();

        if(result.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

    // 주간 출석 통계 조회 (월~일)
    public List<Object[]> findWeeklyStatistics(){

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek currDayOfWeek = now.getDayOfWeek();     // 현재 요일 (1:월 7:일)
        LocalDate currDate = now.toLocalDate();
        LocalDate startOfWeek = currDate.minusDays(currDayOfWeek.getValue() - 1);       // 이번주 시작일 (월)
        LocalDate endOfWeek = currDate.plusDays(6);                                     // 이번주 종료일 (일)

        List<Object[]> result = entityManager.createQuery(
                        "select a.member, count(a) from Attendance a where a.time  >=: startOfWeek and a.time <=: endOfWeek " +
                                "group by a.member", Object[].class)
                .setParameter("startOfWeek", startOfWeek.atStartOfDay())
                .setParameter("endOfWeek", endOfWeek.atTime(23, 59))
                .getResultList();

        return result;
    }

    // 월간 출석 통계 조회
    public List<Object[]> findMonthlyStatistics(){

        LocalDateTime now = LocalDateTime.now();
        LocalDate startDayOfMonth = now.toLocalDate().withDayOfMonth(1);       // 이번달 시작일
        LocalDate endDayOfMonth = now.toLocalDate().withDayOfMonth(now.toLocalDate().lengthOfMonth());  // 이번달 종료일

        List<Object[]> result = entityManager.createQuery(
                        "select a.member, count(a) from Attendance a where a.time  >=: startDayOfMonth and a.time <=: endDayOfMonth " +
                                "group by a.member", Object[].class)
                .setParameter("startDayOfMonth", startDayOfMonth.atStartOfDay())
                .setParameter("endDayOfMonth", endDayOfMonth.atTime(23, 59))
                .getResultList();

        return result;
    }

    //====================================================================================//
}
