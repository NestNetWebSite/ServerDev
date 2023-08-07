package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhotoPostDto {

    private Long id;
    private String title;
    private String bodyContent;
    private Long viewCount;
    private String username;
}
