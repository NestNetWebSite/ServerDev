package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.dto.response.PhotoPostResponse;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.PhotoPostService;
import NestNet.NestNetWebSite.service.post.ThumbNailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PhotoPostController {

    private final PhotoPostService photoPostService;
    private final ThumbNailService thumbNailService;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    /*
    사진 게시판 게시물 저장
     */
    @PostMapping("/photo-post/post")
    public void savePost(@RequestPart("data") @Valid PhotoPostRequest photoPostRequest, @RequestPart("photo-file") List<MultipartFile> files,
                         @AuthenticationPrincipal UserDetails userDetails){

        photoPostService.savePost(photoPostRequest, files, userDetails.getUsername());
    }

    /*
    사진 게시판 목록(썸네일) 조회
     */
    @GetMapping("/photo-post")
    public ApiResult<?> showThumbNail(@RequestParam("offset") int offset, @RequestParam("limit") int limit){

        return thumbNailService.findAllThumbNail(offset, limit);
    }

    /*
    사진 게시물 조회
     */
    @GetMapping("/photo-post/{post_id}")
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId,
                              @AuthenticationPrincipal UserDetails userDetails){

        Map<String, Object> result = new HashMap<>();

        PhotoPostResponse photoPostResponse = photoPostService.findPostById(postId, userDetails.getUsername());
        List<AttachedFileResponse> fileDtoList = attachedFileService.findAllFilesByPost(postId);
        List<CommentResponse> commentResponseList = commentService.findCommentByPost(postId, userDetails.getUsername());
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(postId, userDetails.getUsername());

        result.put("post-data", photoPostResponse);
        result.put("file-data", fileDtoList);
        result.put("comment-data", commentResponseList);
        result.put("is-member-liked", isMemberLiked);

        return ApiResult.success(result);
    }

    /*
    사진 게시물 수정
     */

    /*
    좋아요 누름
     */
    @GetMapping("/photo-post/{post_id}/like")
    public void like(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.saveLike(postId, userDetails.getUsername());
        photoPostService.like(postId);
    }

    /*
    좋아요 취소
     */
    @GetMapping("/photo-post/{post_id}/cancel_like")
    public void dislike(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.cancelLike(postId, userDetails.getUsername());
        photoPostService.cancelLike(postId);
    }
}
