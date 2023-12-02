package NestNet.NestNetWebSite.dto.response.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttendanceStatisticsDto {

    private String memberName;
    private Long point;

    public AttendanceStatisticsDto(String memberName, Long attendanceNum) {
        this.memberName = memberName;
        this.point = attendanceNum * 10;
    }
}
