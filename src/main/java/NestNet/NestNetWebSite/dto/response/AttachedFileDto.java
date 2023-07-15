package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Getter
@AllArgsConstructor
@Builder
public class AttachedFileDto {

    private String originalFileName;
    private String saveFileName;
    private String filePath;
}
