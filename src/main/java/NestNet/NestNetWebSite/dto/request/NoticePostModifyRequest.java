package NestNet.NestNetWebSite.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticePostModifyRequest {

    private Long id;
    private String title;
    private String bodyContent;
}
