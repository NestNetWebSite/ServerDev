package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class RecentPostListResponse {

    private List<RecentPostListDto> dtoList = new ArrayList<>();
}
