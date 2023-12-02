package NestNet.NestNetWebSite.dto.response.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class AttendanceStatisticsResponse {

    List<AttendanceStatisticsDto> dtoList = new ArrayList<>();
}
