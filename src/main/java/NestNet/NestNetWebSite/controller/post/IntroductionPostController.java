package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.IntroductionPostRequest;
import NestNet.NestNetWebSite.service.post.IntroductionPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "시험 기출 게시글 API")
public class IntroductionPostController {

    private final IntroductionPostService introductionPostService;

    @PostMapping("/introduction-post/post")
    @Operation(summary = "자기 소개 게시물 단건 저장", description = "자기 소개 게시물을 저장한다. 파일 저장에 실패한 경우 500 에러를 반환한다.")
    public ApiResult<?> savePost(@RequestPart("data") @Valid IntroductionPostRequest introductionPostRequest,
                                 @RequestPart(value = "file", required = false) List<MultipartFile> file,
                                 @AuthenticationPrincipal UserDetails userDetails){

        return introductionPostService.savePost(introductionPostRequest, file, userDetails.getUsername());
    }

}
