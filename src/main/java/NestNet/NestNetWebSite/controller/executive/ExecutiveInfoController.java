package NestNet.NestNetWebSite.controller.executive;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.ExecutiveInfoRequest;
import NestNet.NestNetWebSite.service.executive.ExecutiveInfoService;
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
    public void saveInfo(@RequestBody List<ExecutiveInfoRequest> executiveInfoRequestList){

        executiveInfoService.saveExecutiveInfo(executiveInfoRequestList);
    }

    /*
    전 임원 정보 조회
     */
    @GetMapping("/executive-info/prev")
    public ApiResult<?> findPrevInfo(){

        return executiveInfoService.findPrevExecutiveList();
    }

    /*
    현 임원 정보 조회
     */
    @GetMapping("/executive-info/current")
    public ApiResult<?> findCurrentInfo(){

        return executiveInfoService.findCurrentExecutiveList();
    }

}
