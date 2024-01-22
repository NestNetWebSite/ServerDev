package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.NoticePostModifyRequest;
import NestNet.NestNetWebSite.dto.request.NoticePostRequest;
import NestNet.NestNetWebSite.service.post.NoticePostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "공지 사항 게시글 API")
public class NoticePostController {

    private final NoticePostService noticePostService;

    /*
    공지 사항 게시물 단건 저장
     */
    @PostMapping("/notice-post/post")
    @Operation(summary = "공지 사항 게시물 단건 저장", description = "공지 사항 게시물을 저장한다. 파일 저장에 실패한 경우 500 에러를 반환한다.")
    public ApiResult<?> savePost(@RequestPart("data") NoticePostRequest noticePostRequest,
                                 @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                 @AuthenticationPrincipal UserDetails userDetails){

        return noticePostService.savePost(noticePostRequest, files, userDetails.getUsername());
    }

    /*
    공지 사항 게시물 목록 조회
     */
    @GetMapping("/notice-post")
    @Operation(summary = "공지 사항 게시물 목록 조회", description = "공지 사항 게시물 목록을 조회한다.")
    public ApiResult<?> showPostList(@RequestParam("page") int page, @RequestParam("size") int size){

        return noticePostService.findPostListByPaging(page, size);
    }

    /*
    공지 사항 게시물 단건 조회
     */
    @GetMapping("/notice-post/{post_id}")
    @Operation(summary = "공지 사항 게시물 단건 조회", description = "공지 사항 게시물을 조회한다.")
    public ApiResult<?>  showPost(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        return noticePostService.findPostById(postId, userDetails.getUsername());
    }

    /*
    공지 사항 게시판 게시물 수정
     */
    @PostMapping("/notice-post/modify")
    @Operation(summary = "공지 사항 게시판 게시물 수정", description = "파일 저장에 문제가 생기는 경우 500 에러를 반환한다.")
    public ApiResult<?> modifyPost(@RequestPart("data") NoticePostModifyRequest modifyRequest,
                                   @RequestPart(value = "file-id", required = false) List<Long> fileIdList,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files){

        noticePostService.modifyPost(modifyRequest, fileIdList, files);

        return ApiResult.success("게시물 수정 완료");
    }
}
