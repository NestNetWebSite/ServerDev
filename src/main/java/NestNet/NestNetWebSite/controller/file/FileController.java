package NestNet.NestNetWebSite.controller.file;

import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final AttachedFileService attachedFileService;

    /*
    파일 조회 (다운로드)
     */
    @GetMapping("/file/{postId}/{fileName}")
    public ResponseEntity<InputStreamResource> showFile(@PathVariable(value = "postId") Long postId, @PathVariable(value = "fileName") String fileName){

        InputStreamResource inputStreamResource = attachedFileService.findFile(postId, fileName);

        HttpHeaders headers = new HttpHeaders();
        //파일 다운로드 시 클라이언트에게 이진 파일임을 알리고, 브라우저는 파일 다운로드 대화 상자를 표시하거나 다운로드 링크를 제공하여 사용자가 파일을 다운로드하도록 할 수 있습니다.
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);


        // 파일 스트림을 ResponseEntity로 감싸서 전송
        return ResponseEntity.ok()
                .headers(headers)
                .body(inputStreamResource);
    }


}
