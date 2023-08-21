package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AttachedFileResponse {

    private String originalFileName;
    private String saveFileName;
    private String filePath;
}
