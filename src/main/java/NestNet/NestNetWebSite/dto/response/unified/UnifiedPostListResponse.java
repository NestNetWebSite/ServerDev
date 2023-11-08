package NestNet.NestNetWebSite.dto.response.unified;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class UnifiedPostListResponse {

    Long totalSize;
    List<UnifiedPostListDto> dtoList = new ArrayList<>();
}
