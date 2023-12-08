package NestNet.NestNetWebSite.service.attendance;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attendance.Attendance;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.dto.response.attendance.AttendanceStatisticsDto;
import NestNet.NestNetWebSite.dto.response.attendance.AttendanceStatisticsResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.attendance.AttendanceRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    /*
    출석 -> 0시~24시까지 한번 가능
     */
    @Transactional
    public ApiResult<?> saveAttendance(String memberLonginId, HttpServletResponse response){

        Member member = memberRepository.findByLoginId(memberLonginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX);

        Optional<Attendance> prevAttendance = attendanceRepository.findByMemberAndDay(member, startOfDay, endOfDay);

        if(prevAttendance.isPresent()){
            throw new CustomException(ErrorCode.ALREADY_ATTENDED);
        }

        Attendance attendance = new Attendance(member);
        attendanceRepository.save(attendance);

        return ApiResult.success("출석하셨습니다.");
    }

    /*
    주간 출석 조회
     */
    public ApiResult<?> findWeeklyAttendanceStatistics(){

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek currDayOfWeek = now.getDayOfWeek();     // 현재 요일 (1:월 7:일)
        LocalDate currDate = now.toLocalDate();
        LocalDateTime startDateTimeOfWeek = currDate.minusDays(currDayOfWeek.getValue() - 1).atStartOfDay();       // 이번주 시작일(Mon)의 시작 시간
        LocalDateTime endDateTimeOfWeek = currDate.plusDays(6).atTime(23, 59);                        // 이번주 종료일(Sun)의 끝나는 시간

        System.out.println(startDateTimeOfWeek + " " + endDateTimeOfWeek);

        List<Object[]> statistics = attendanceRepository.findWeeklyCount(startDateTimeOfWeek, endDateTimeOfWeek);

        List<AttendanceStatisticsDto> statisticsDtoList = new ArrayList<>();

        for(Object[] row : statistics){
            Member member = (Member)row[0];
            statisticsDtoList.add(new AttendanceStatisticsDto(member.getName(), Long.valueOf(String.valueOf(row[1]))));
        }

        return ApiResult.success(new AttendanceStatisticsResponse(statisticsDtoList));

    }

    /*
    월간 출석 조회
     */
    public ApiResult<?> findMonthlyAttendanceStatistics(){

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTimeOfMonth = now.toLocalDate().withDayOfMonth(1).atStartOfDay();       // 이번달 시작일의 시작 시간
        LocalDateTime endDateTimeOfMonth = now.toLocalDate().withDayOfMonth(now.toLocalDate().lengthOfMonth())
                .atTime(23, 59);  // 이번달 종료일의 끝나는 시간

        List<Object[]> statistics = attendanceRepository.findMonthlyCount(startDateTimeOfMonth, endDateTimeOfMonth);

        List<AttendanceStatisticsDto> statisticsDtoList = new ArrayList<>();

        for(Object[] row : statistics){
            Member member = (Member)row[0];
            statisticsDtoList.add(new AttendanceStatisticsDto(member.getName(), Long.valueOf(String.valueOf(row[1]))));
        }

        return ApiResult.success(new AttendanceStatisticsResponse(statisticsDtoList));
    }

}
