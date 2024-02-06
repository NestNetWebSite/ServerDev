package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveInfoResponse {

    private List<ExecutiveInfoDto> dtoList = new ArrayList<>();
}
