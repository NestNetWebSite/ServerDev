package NestNet.NestNetWebSite.controller.life4cut;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.service.life4cut.Life4CutService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Life4CutController {

    private final Life4CutService life4CutService;

    /*
    인생네컷 저장
     */
    @PostMapping( "/life4cut/save")
    public ApiResult<?> save(@RequestPart("file") MultipartFile file, HttpServletResponse response){

        return life4CutService.saveFile(file, response);
    }

    /*
    인생네컷 랜덤 조회
     */
    @GetMapping("/life4cut/{limit}")
    public ApiResult<?> showLife4Cut(@PathVariable("limit") int limit){

        return life4CutService.findFileByRandom(limit);
    }
}
