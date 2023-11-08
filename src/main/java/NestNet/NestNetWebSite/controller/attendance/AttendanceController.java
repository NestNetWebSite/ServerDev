package NestNet.NestNetWebSite.controller.attendance;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.service.attendance.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "출석 API")
public class AttendanceController {

    private final AttendanceService attendanceService;

    /*
    출석
     */
    @GetMapping("/attendance")
    @Operation(summary = "출석", description = "로그인한 사용자가 출석 버튼을 눌렀을 때 동작한다.")
    public ApiResult<?> memberAttendance(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){

        return attendanceService.saveAttendance(userDetails.getUsername(), response);
    }
}
