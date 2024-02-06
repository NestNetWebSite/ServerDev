package NestNet.NestNetWebSite.dto.response.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class AttendanceStatisticsResponse {

    private List<WeeklyAttendanceStatisticsDto> weeklyStatisticsDtoList = new ArrayList<>();
    private List<MonthlyAttendanceStatisticsDto> monthlyStatisticsDtoList = new ArrayList<>();
}
