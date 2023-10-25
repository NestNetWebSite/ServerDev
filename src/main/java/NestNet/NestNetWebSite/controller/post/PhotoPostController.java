package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.PhotoPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequest;
import NestNet.NestNetWebSite.dto.request.PostLikeRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.dto.response.PhotoPostResponse;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.PhotoPostService;
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
public class PhotoPostController {

    private final PhotoPostService photoPostService;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    /*
    사진 게시판 게시물 저장
     */
    @PostMapping("/photo-post/post")
    public ApiResult<?> savePost(@RequestPart("data") @Valid PhotoPostRequest photoPostRequest, @RequestPart("photo-file") List<MultipartFile> files,
                         HttpServletResponse response){

        return photoPostService.savePost(photoPostRequest, files, "manager", response);
    }
//    @PostMapping("/photo-post/post")
//    public void savePost(@RequestPart("data") @Valid PhotoPostRequest photoPostRequest, @RequestPart("photo-file") List<MultipartFile> files,
//                         @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){
//
//        photoPostService.savePost(photoPostRequest, files, userDetails.getUsername(), response);
//    }

    /*
    사진 게시판 목록(썸네일) 조회
     */
    @GetMapping("/photo-post")
    public ApiResult<?> showThumbNail(@RequestParam("offset") int offset, @RequestParam("limit") int limit){

        return photoPostService.findThumbNails(offset, limit);
    }


    /*
    사진 게시물 단건 조회
     */
    @GetMapping("/photo-post/{post_id}")
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId){

        Map<String, Object> result = new HashMap<>();

        PhotoPostResponse photoPostResponse = photoPostService.findPostById(postId, "manager");
        List<AttachedFileResponse> fileDtoList = attachedFileService.findAllFilesByPost(postId);
        List<CommentResponse> commentResponseList = commentService.findCommentByPost(postId, "manager");
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(postId, "manager");

        result.put("post-data", photoPostResponse);
        result.put("file-data", fileDtoList);
        result.put("comment-data", commentResponseList);
        result.put("is-member-liked", isMemberLiked);

        return ApiResult.success(result);
    }

    /*
    사진 게시물 수정
     */
    @PostMapping("/photo-post/modify/{post_id}")
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
        boolean isCompleted = attachedFileService.modifyFiles(fileIdList, files, postId);

        if(isCompleted) return ApiResult.success("게시물 수정 완료");
        else return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "파일 수정 에러");
    }

    /*
    사진 게시물 삭제 -> 게시물, 첨부파일, 댓글, 좋아요 모두 삭제
     */
    @DeleteMapping("/photo-post/delete")
    public ApiResult<?> deletePost(@RequestParam(value = "postId") Long postId, HttpServletResponse response){

        boolean isComplete = attachedFileService.deleteFiles(postId);
        photoPostService.deletePost(postId);
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
    @PostMapping("/photo-post/like")
    public void like(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.saveLike(request.getPostId(), userDetails.getUsername());
        photoPostService.like(request.getPostId());
    }

    /*
    좋아요 취소
     */
    @PostMapping("/photo-post/cancel-like")
    public void dislike(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.cancelLike(request.getPostId(), userDetails.getUsername());
        photoPostService.cancelLike(request.getPostId());
    }
}
