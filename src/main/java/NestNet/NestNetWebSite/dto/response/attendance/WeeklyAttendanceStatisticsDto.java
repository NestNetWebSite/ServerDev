package NestNet.NestNetWebSite.dto.response.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyAttendanceStatisticsDto {

    private String memberName;
    private String type;
    private Long point;
}
