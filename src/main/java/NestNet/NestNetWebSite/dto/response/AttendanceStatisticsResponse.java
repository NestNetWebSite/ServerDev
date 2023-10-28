package NestNet.NestNetWebSite.dto.response;

import lombok.Getter;

@Getter
public class AttendanceStatisticsResponse {

    private String memberName;
    private Long point;


    public AttendanceStatisticsResponse(String memberName, Long attendanceNum) {
        this.memberName = memberName;
        this.point = attendanceNum * 10;
    }
}
