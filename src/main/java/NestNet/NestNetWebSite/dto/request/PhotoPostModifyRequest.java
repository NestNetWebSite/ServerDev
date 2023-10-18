package NestNet.NestNetWebSite.dto.request;

import lombok.Getter;

@Getter
public class PhotoPostModifyRequest {

    private Long id;
    private String title;
    private String bodyContent;
}
