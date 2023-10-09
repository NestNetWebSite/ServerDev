package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AttachedFileResponse {

    private Long id;
    private String originalFileName;
    private String saveFilePath;
    private String saveFileName;

}
