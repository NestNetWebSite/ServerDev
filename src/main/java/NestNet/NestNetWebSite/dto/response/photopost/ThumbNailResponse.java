package NestNet.NestNetWebSite.dto.response.photopost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ThumbNailResponse {

    private List<ThumbNailDto> dtoList = new ArrayList<>();
}
