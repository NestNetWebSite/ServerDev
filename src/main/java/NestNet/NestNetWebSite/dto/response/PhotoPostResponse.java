package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhotoPostResponse {

    private Long id;
    private String title;
    private String bodyContent;
    private Long viewCount;
    private int likeCount;
    private String username;
}
