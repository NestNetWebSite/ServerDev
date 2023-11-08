package NestNet.NestNetWebSite.controller.file;

import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "파일 다운로드 API")
public class FileController {

    private final AttachedFileService attachedFileService;

    /*
    파일 다운로드
     */
    @GetMapping("/file/{postId}/{fileName}")
    @Operation(summary = "파일 다운로드", description = "파일 다운로드 요청에 응답하여 인코딩된 파일을 전송한다.", responses = {
                    @ApiResponse(responseCode = "200", description = "파일 다운로드 성공", content = @Content(schema = @Schema(implementation = InputStreamResource.class)))
            })
    public ResponseEntity<InputStreamResource> showFile(@PathVariable(value = "postId") Long postId, @PathVariable(value = "fileName") String fileName){

        InputStreamResource inputStreamResource = attachedFileService.findFile(postId, fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // 파일 스트림을 ResponseEntity로 감싸서 전송
        return ResponseEntity.ok()
                .headers(headers)
                .body(inputStreamResource);
    }
}
