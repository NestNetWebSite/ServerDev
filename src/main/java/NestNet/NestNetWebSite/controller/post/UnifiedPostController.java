package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.dto.request.UnifiedPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequest;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostListResponse;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostResponse;
import NestNet.NestNetWebSite.service.post.UnifiedPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "통합 게시글 API")
public class UnifiedPostController {

    private final UnifiedPostService unifiedPostService;

    /*
    통합 게시판 게시물 저장
     */
    @PostMapping("/unified-post/post")
    @Operation(summary = "통합 게시판 게시물 단건 저장", description = "통합 게시판 게시물을 저장한다. 파일 저장에 실패한 경우 500 에러를 반환한다.")
    public ApiResult<?> savePost(@RequestPart("data") @Valid UnifiedPostRequest unifiedPostRequest,
                                 @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                 @AuthenticationPrincipal UserDetails userDetails){

        return unifiedPostService.savePost(unifiedPostRequest, files, userDetails.getUsername());
    }

    /*
    통합 게시판 Type에 따른 목록 조회 (FREE, DEV, CAREER)
     */
    @GetMapping("/unified-post")
    @Operation(summary = "통합 게시판 게시물 목록 조회", description = "자유/개발/진로 를 필터링(필수 아님)할 수 있다.", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = UnifiedPostListResponse.class)))
    })
    public ApiResult<?> showPostListByType(@RequestParam(value = "post-type", required = false) UnifiedPostType unifiedPostType,
                                        @RequestParam("page") int page, @RequestParam("size") int size){

        return unifiedPostService.findPostList(unifiedPostType, page, size);
    }

    /*
    통합 게시판 게시물 단건 상세 조회
     */
    @GetMapping("/unified-post/{post_id}")
    @Operation(summary = "통합 게시판 게시물 단건 상세 조회", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = UnifiedPostResponse.class)))
    })
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        UnifiedPostResponse result = unifiedPostService.findPostById(postId, userDetails.getUsername());

        return ApiResult.success(result);
    }

    /*
    통합 게시물 수정
     */
    @PostMapping("/unified-post/modify")
    @Operation(summary = "통합 게시판 게시물 수정", description = "로그인한 사용자가 자신이 작성한 통합 게시물을 수정한다. 파일 수정에서 문제가 발생할 경우 500 에러를 반환한다.")

    public ApiResult<?> modifyPost(@RequestPart("data") UnifiedPostModifyRequest unifiedPostModifyRequest,
                                   @RequestPart(value = "file-id", required = false) List<Long> fileIdList,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files){

        unifiedPostService.modifyPost(unifiedPostModifyRequest, fileIdList, files);

        return ApiResult.success("게시물 수정 완료");
    }

}
