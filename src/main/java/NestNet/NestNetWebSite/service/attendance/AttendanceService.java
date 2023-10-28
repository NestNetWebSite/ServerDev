package NestNet.NestNetWebSite.service.attendance;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attendance.Attendance;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.repository.attendance.AttendanceRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    /*
    출석
     */
    @Transactional
    public ApiResult<?> saveAttendance(String memberLonginId, HttpServletResponse response){

        Member member = memberRepository.findByLoginId(memberLonginId);

        if(attendanceRepository.isMemberAttended(member)){
            return ApiResult.error(response, HttpStatus.BAD_REQUEST, "이미 출석하셨습니다.");
        }
        else{
            Attendance attendance = new Attendance(member);
            attendanceRepository.save(attendance);
        }
        return ApiResult.success("출석하셨습니다.");
    }

}
