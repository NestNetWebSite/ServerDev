package NestNet.NestNetWebSite.controller.file;

import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import lombok.RequiredArgsConstructor;
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
    파일 조회
     */
//    @GetMapping("/file/{postId}/{fileName}")
//    public FileSystemResource showFile(@PathVariable(value = "postId") Long postId, @PathVariable(value = "fileName") String fileName){
//
//        return attachedFileService.findFile(postId, fileName);
//    }
    @GetMapping("/file/{postId}/{fileName}")
    public ResponseEntity<InputStreamResource> showFile(@PathVariable(value = "postId") Long postId, @PathVariable(value = "fileName") String fileName){

        InputStreamResource inputStreamResource = attachedFileService.findFile(postId, fileName);

        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.txt");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // 파일 스트림을 ResponseEntity로 감싸서 전송
        return ResponseEntity.ok()
                .headers(headers)
//                .contentLength(file.length())
                .body(inputStreamResource);
    }
}
