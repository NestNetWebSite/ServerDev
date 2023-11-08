package NestNet.NestNetWebSite.controller.life4cut;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.response.Life4CutResponse;
import NestNet.NestNetWebSite.service.life4cut.Life4CutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "인생네컷 API")
public class Life4CutController {

    private final Life4CutService life4CutService;

    /*
    인생네컷 저장
     */
    @PostMapping( "/life4cut/save")
    @Operation(summary = "인생네컷 사진 단건 저장", description = "인생네컷 저장은 회장, 부회장, 매니저만 접근 가능하다. 저장 실패 시 500 에러를 반환한다.")
    public ApiResult<?> save(@RequestPart("file") MultipartFile file, HttpServletResponse response){

        return life4CutService.saveFile(file, response);
    }

    /*
    인생네컷 랜덤 조회
     */
    @GetMapping("/life4cut/{limit}")
    @Operation(summary = "인생네컷 다건 조회", description = "size(갯수)만큼 인생네컷을 랜덤으로 조회하여 반환한다.", responses = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = Life4CutResponse.class)))
    })
    public ApiResult<?> showLife4Cut(@PathVariable("size") int size){

        return life4CutService.findFileByRandom(size);
    }
}
