package NestNet.NestNetWebSite.dto.response.photopost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PhotoFileDto {

    private Long id;
    private String originalFileName;
    private String saveFilePath;
    private String saveFileName;

}
