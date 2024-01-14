package NestNet.NestNetWebSite.dto.response.introductionpost;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class IntroductionPostListResponse {

    private Long totalSize;
    private List<IntroductionPostListDto> dtoList = new ArrayList<>();
}
