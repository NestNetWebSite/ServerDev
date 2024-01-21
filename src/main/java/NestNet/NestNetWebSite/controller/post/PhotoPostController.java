package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.PhotoPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequest;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoPostResponse;
import NestNet.NestNetWebSite.dto.response.photopost.ThumbNailResponse;
import NestNet.NestNetWebSite.service.post.PhotoPostService;
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
@Tag(name = "사진 게시글 API")
public class PhotoPostController {

    private final PhotoPostService photoPostService;

    /*
    사진 게시판 게시물 저장
     */
    @PostMapping("/photo-post/post")
    @Operation(summary = "사진 게시판 게시물 저장", description = "")
    public ApiResult<?> savePost(@RequestPart("data") @Valid PhotoPostRequest photoPostRequest,
                                 @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                 @AuthenticationPrincipal UserDetails userDetails){

        // 허원일 테스트용
        return photoPostService.savePost(photoPostRequest, files, "admin");

//        return photoPostService.savePost(photoPostRequest, files, userDetails.getUsername());
    }

    /*
    사진 게시판 목록(썸네일) 조회
     */
    @GetMapping("/photo-post")
    @Operation(summary = "사진 게시판 게시물 목록(썸네일) 조회", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = ThumbNailResponse.class)))
    })
    public ApiResult<?> showThumbNail(@RequestParam("page") int page){

        int size = 12;

        return photoPostService.findThumbNail(page, size);
    }


    /*
    사진 게시물 단건 조회
     */
    @GetMapping("/photo-post/{post_id}")
    @Operation(summary = "사진 게시판 게시물 단건 조회", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = PhotoPostResponse.class)))
    })
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        // 허원일 테스트용
        PhotoPostResponse result = photoPostService.findPostById(postId, "admin");

//        PhotoPostResponse result = photoPostService.findPostById(postId, userDetails.getUsername());

        return ApiResult.success(result);
    }

    /*
    사진 게시물 수정
     */
    @PostMapping("/photo-post/modify")
    @Operation(summary = "사진 게시판 게시물 수정", description = "로그인한 사용자가 자신이 작성한 사진 게시물을 수정한다. 파일 수정에서 문제가 발생할 경우 500 에러를 반환한다.")
    public ApiResult<?> modifyPost(@RequestPart("data") PhotoPostModifyRequest photoPostModifyRequest,
                                   @RequestPart(value = "file-id", required = false) List<Long> fileIdList,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files){


        photoPostService.modifyPost(photoPostModifyRequest, fileIdList, files);

        return ApiResult.success("게시물 수정 완료");
    }

}
