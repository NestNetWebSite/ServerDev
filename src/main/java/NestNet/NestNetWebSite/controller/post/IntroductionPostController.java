package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.IntroductionPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.IntroductionPostRequest;
import NestNet.NestNetWebSite.service.post.IntroductionPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "자기 소개 게시글 API")
public class IntroductionPostController {

    private final IntroductionPostService introductionPostService;

    /*
    자기 소개 게시물 단건 저장
     */
    @PostMapping("/introduction-post/post")
    @Operation(summary = "자기 소개 게시물 단건 저장", description = "자기 소개 게시물을 저장한다. 파일 저장에 실패한 경우 500 에러를 반환한다.")
    public ApiResult<?> savePost(@RequestPart("data") @Valid IntroductionPostRequest introductionPostRequest,
                                 @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                 @AuthenticationPrincipal UserDetails userDetails){

        return introductionPostService.savePost(introductionPostRequest, files, userDetails.getUsername());
    }

    /*
    자기 소개 게시물 목록 조회
     */
    @GetMapping("/introduction-post")
    @Operation(summary = "자기 소개 게시물 목록 조회", description = "자기 소개 게시물 목록을 조회한다.")
    public ApiResult<?> showPostList(@RequestParam("page") int page, @RequestParam("size") int size){

        return introductionPostService.findPostListByPaging(page, size);
    }

    /*
    자기 소개 게시물 단건 조회
     */
    @GetMapping("/introduction-post/{post_id}")
    @Operation(summary = "자기 소개 게시물 단건 조회", description = "자기 소개 게시물을 조회한다.")
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        return introductionPostService.findPostById(postId, userDetails.getUsername());
    }

    /*
    자기 소개 게시판 게시물 수정
     */
    @PostMapping("/introduction-post/modify")
    @Operation(summary = "자기 소개 게시판 게시물 수정", description = "파일 저장에 문제가 생기는 경우 500 에러를 반환한다.")
    public ApiResult<?> modifyPost(@RequestPart("data") IntroductionPostModifyRequest modifyRequest,
                                   @RequestPart(value = "file-id", required = false) List<Long> fileIdList,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files){

        introductionPostService.modifyPost(modifyRequest, fileIdList, files);

        return ApiResult.success("게시물 수정 완료");
    }

}
