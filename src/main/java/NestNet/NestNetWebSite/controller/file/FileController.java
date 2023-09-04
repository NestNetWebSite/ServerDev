package NestNet.NestNetWebSite.controller.file;

import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final AttachedFileService attachedFileService;

    /*
    파일 조회
     */
    @GetMapping("/file")
    public FileSystemResource showFile(@RequestParam(value = "postId") Long postId, @RequestParam(value = "fileName") String saveFileName){

        return attachedFileService.findFile(postId, saveFileName);
    }
}
