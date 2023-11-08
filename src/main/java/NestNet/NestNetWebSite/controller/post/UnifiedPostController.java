package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.dto.request.PostLikeRequest;
import NestNet.NestNetWebSite.dto.request.UnifiedPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostResponse;
import NestNet.NestNetWebSite.dto.response.unified.UnifiedPostDto;
import NestNet.NestNetWebSite.dto.response.unified.UnifiedPostListResponse;
import NestNet.NestNetWebSite.dto.response.unified.UnifiedPostResponse;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostListResponse;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.UnifiedPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "통합 게시글 API")
public class UnifiedPostController {

    private final UnifiedPostService unifiedPostService;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    /*
    통합 게시판 게시물 저장
     */
    @PostMapping("unified-post/post")
    @Operation(summary = "통합 게시판 게시물 단건 저장", description = "통합 게시판 게시물을 저장한다. 파일 저장에 실패한 경우 500 에러를 반환한다.")
    public ApiResult<?> savePost(@RequestPart("data") @Valid UnifiedPostRequest unifiedPostRequest, @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                 @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){

        for(MultipartFile file : files){
            System.out.println(file.getName());
        }

        return unifiedPostService.savePost(unifiedPostRequest, files, userDetails.getUsername(), response);
    }

    /*
    통합 게시판 Type에 따른 목록 조회 (FREE, DEV, CAREER)
     */
    @GetMapping("unified-post")
    @Operation(summary = "통합 게시판 게시물 목록 조회", description = "자유/개발/진로 를 필터링(필수 아님)할 수 있다.", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = UnifiedPostListResponse.class)))
    })
    public ApiResult<?> showPostListByType(@RequestParam(value = "post-type", required = false) UnifiedPostType unifiedPostType,
                                        @RequestParam("offset") int offset, @RequestParam("limit") int limit){

        return unifiedPostService.findPostList(unifiedPostType, offset, limit);
    }

    /*
    통합 게시판 게시물 단건 상세 조회
     */
    @GetMapping("/unified-post/{post_id}")
    @Operation(summary = "통합 게시판 게시물 단건 상세 조회", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = UnifiedPostResponse.class)))
    })
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        UnifiedPostDto postDto = unifiedPostService.findPostById(postId, userDetails.getUsername());
        List<AttachedFileResponse> fileDtoList = attachedFileService.findAllFilesByPost(postId);
        List<CommentResponse> commentResponseList = commentService.findCommentByPost(postId, userDetails.getUsername());
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(postId, userDetails.getUsername());

        UnifiedPostResponse result = new UnifiedPostResponse(postDto, fileDtoList, commentResponseList, isMemberLiked);

        return ApiResult.success(result);
    }

    /*
    통합 게시물 수정
     */
    @PostMapping("unified-post/modify")
    @Operation(summary = "통합 게시판 게시물 수정", description = "로그인한 사용자가 자신이 작성한 통합 게시물을 수정한다. 파일 수정에서 문제가 발생할 경우 500 에러를 반환한다.")

    public ApiResult<?> modifyPost(@RequestPart("data") UnifiedPostModifyRequest unifiedPostModifyRequest,
                                   @RequestPart(value = "file-id", required = false) List<Long> fileIdList,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                   HttpServletResponse response){

        if(fileIdList == null){
            fileIdList = new ArrayList<>();
        }
        if(files == null){
            files = new ArrayList<>();
        }

        unifiedPostService.modifyPost(unifiedPostModifyRequest);
        boolean isCompleted = attachedFileService.modifyFiles(fileIdList, files, unifiedPostModifyRequest.getId());

        if(isCompleted) return ApiResult.success("게시물 수정 완료");
        else return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "파일 수정 에러");
    }

    /*
    통합 게시물 삭제 -> 게시물, 첨부파일, 댓글, 좋아요 모두 삭제
     */
    @DeleteMapping("/unified-post/delete")
    @Operation(summary = "사진 게시판 게시물 삭제", description = "파일 삭제에 문제가 생기는 경우 500 에러를 반환한다.")
    public ApiResult<?> deletePost(@RequestParam(value = "postId") Long postId, HttpServletResponse response){

        boolean isComplete = attachedFileService.deleteFiles(postId);
        unifiedPostService.deletePost(postId);
        commentService.deleteAllComments(postId);
        postLikeService.deleteLike(postId);

        if(isComplete){
            return ApiResult.success("게시물 삭제 완료");
        }
        else{
            return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "게시물 삭제 실패. 서버 에러");
        }
    }

    /*
    좋아요 누름
     */
    @PostMapping("/unified-post/like")
    @Operation(summary = "좋아요", description = "")
    public void like(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.saveLike(request.getPostId(), userDetails.getUsername());
        unifiedPostService.like(request.getPostId());
    }

    /*
    좋아요 취소
     */
    @PostMapping("/unified-post/cancel-like")
    @Operation(summary = "좋아요 취소", description = "")
    public void dislike(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.cancelLike(request.getPostId(), userDetails.getUsername());
        unifiedPostService.cancelLike(request.getPostId());
    }

}
