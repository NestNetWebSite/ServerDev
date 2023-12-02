package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.PhotoPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequest;
import NestNet.NestNetWebSite.dto.request.PostLikeRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoPostDto;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoPostResponse;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostResponse;
import NestNet.NestNetWebSite.dto.response.photopost.ThumbNailResponse;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.PhotoPostService;
import NestNet.NestNetWebSite.service.post.PostService;
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
@Tag(name = "사진 게시글 API")
public class PhotoPostController {

    private final PhotoPostService photoPostService;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
    private final PostService postService;

    /*
    사진 게시판 게시물 저장
     */
    @PostMapping("/photo-post/post")
    @Operation(summary = "사진 게시판 게시물 저장", description = "")
    public void savePost(@RequestPart("data") @Valid PhotoPostRequest photoPostRequest, @RequestPart("photo-file") List<MultipartFile> files,
                         @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){

        photoPostService.savePost(photoPostRequest, files, userDetails.getUsername(), response);
    }

    /*
    사진 게시판 목록(썸네일) 조회
     */
    @GetMapping("/photo-post")
    @Operation(summary = "사진 게시판 게시물 목록(썸네일) 조회", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = ThumbNailResponse.class)))
    })
    public ApiResult<?> showThumbNail(@RequestParam("offset") int offset, @RequestParam("limit") int limit){

        return photoPostService.findThumbNails(offset, limit);
    }


    /*
    사진 게시물 단건 조회
     */
    @GetMapping("/photo-post/{post_id}")
    @Operation(summary = "사진 게시판 게시물 단건 조회", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = PhotoPostResponse.class)))
    })
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId){

        PhotoPostDto photoPostdto = photoPostService.findPostById(postId, "manager");
        List<AttachedFileResponse> fileDtoList = attachedFileService.findAllFilesByPost(postId);
        List<CommentResponse> commentResponseList = commentService.findCommentByPost(postId, "manager");
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(postId, "manager");

        PhotoPostResponse result = new PhotoPostResponse(photoPostdto, fileDtoList, commentResponseList, isMemberLiked);

        return ApiResult.success(result);
    }

    /*
    사진 게시물 수정
     */
    @PostMapping("/photo-post/modify/{post_id}")
    @Operation(summary = "사진 게시판 게시물 수정", description = "로그인한 사용자가 자신이 작성한 사진 게시물을 수정한다. 파일 수정에서 문제가 발생할 경우 500 에러를 반환한다.")
    public ApiResult<?> modifyPost(@PathVariable("post_id") Long postId,
                                   @RequestPart("data") PhotoPostModifyRequest photoPostModifyRequest,
                                   @RequestPart(value = "file-id", required = false) List<Long> fileIdList,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                   HttpServletResponse response){

        if(fileIdList == null){
            fileIdList = new ArrayList<>();
        }
        if(files == null){
            files = new ArrayList<>();
        }

        photoPostService.modifyPost(photoPostModifyRequest, postId);
        attachedFileService.modifyFiles(fileIdList, files, postId);

        return ApiResult.success("게시물 수정 완료");
    }

    /*
    사진 게시물 삭제 -> 게시물, 첨부파일, 댓글, 좋아요 모두 삭제
     */
    @DeleteMapping("/photo-post/delete")
    @Operation(summary = "사진 게시판 게시물 삭제", description = "파일 삭제에 문제가 생기는 경우 500 에러를 반환한다.")
    public ApiResult<?> deletePost(@RequestParam(value = "postId") Long postId, HttpServletResponse response){

        attachedFileService.deleteFiles(postId);
        photoPostService.deletePost(postId);
        commentService.deleteAllComments(postId);
        postLikeService.deleteLike(postId);

        return ApiResult.success("게시물 삭제 완료");
    }

    /*
    좋아요 누름
     */
    @PostMapping("/photo-post/like")
    @Operation(summary = "좋아요", description = "")
    public void like(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.saveLike(request.getPostId(), userDetails.getUsername());
        postService.like(request.getPostId());
    }

    /*
    좋아요 취소
     */
    @PostMapping("/photo-post/cancel-like")
    @Operation(summary = "좋아요 취소", description = "")
    public void dislike(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.cancelLike(request.getPostId(), userDetails.getUsername());
        postService.cancelLike(request.getPostId());
    }
}
