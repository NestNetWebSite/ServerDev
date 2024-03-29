package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AttachedFileDto {

    private Long id;
    private String originalFileName;
    private String saveFilePath;
    private String saveFileName;

}
