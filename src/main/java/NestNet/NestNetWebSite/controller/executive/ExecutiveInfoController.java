package NestNet.NestNetWebSite.controller.executive;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.ExecutiveInfoModifyRequest;
import NestNet.NestNetWebSite.dto.request.ExecutiveInfoRequest;
import NestNet.NestNetWebSite.dto.response.ExecutiveInfoResponse;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostListResponse;
import NestNet.NestNetWebSite.service.executive.ExecutiveInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "임원 정보 API")
public class ExecutiveInfoController {

    private final ExecutiveInfoService executiveInfoService;

    /*
    임원 정보 저장
     */
    @PostMapping("/executive-info/save")
    @Operation(summary = "임원 정보 다건 저장", description = "임원 정보를 저장한다. 이때, 여러건을 한번에 저장할 수 있다.")
    public void saveInfo(@RequestBody List<ExecutiveInfoRequest> executiveInfoRequestList){

        executiveInfoService.saveExecutiveInfo(executiveInfoRequestList);
    }

    /*
    전 임원 정보 조회
     */
    @GetMapping("/executive-info/prev")
    @Operation(summary = "전 임원 정보 조회", description = "전 임원 정보를 모두 조회한다.", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = ExecutiveInfoResponse.class)))
    })
    public ApiResult<?> findPrevInfo(){

        return executiveInfoService.findPrevExecutiveList();
    }

    /*
    현 임원 정보 조회
     */
    @GetMapping("/executive-info/current")
    @Operation(summary = "현 임원 정보 조회", description = "현 임원 정보를 모두 조회한다.", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = ExecutiveInfoResponse.class)))
    })
    public ApiResult<?> findCurrentInfo(){

        return executiveInfoService.findCurrentExecutiveList();
    }

    /*
    임원 정보 수정
     */
    @PostMapping("/executive-info/modify")
    @Operation(summary = "임원 정보 단건 수정", description = "임원 정보를 단건 수정한다.")
    public ApiResult<?> modifyExecutiveInfo(@RequestBody ExecutiveInfoModifyRequest request){

        return executiveInfoService.modifyExecutiveInfo(request);
    }

    /*
    임원 정보 삭제
     */
    @DeleteMapping("/executive-info/delete")
    @Operation(summary = "임원 정보 단건 삭제", description = "임원 정보를 단건 삭제한다.")
    public ApiResult<?> modifyExecutiveInfo(@RequestParam("executive-id") Long id){

        return executiveInfoService.deleteExecutiveInfo(id);
    }

}
