package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequestDto;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequestDto;
import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import NestNet.NestNetWebSite.dto.response.PhotoPostDto;
import NestNet.NestNetWebSite.dto.response.ThumbNailDto;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.PhotoPostService;
import NestNet.NestNetWebSite.service.post.ThumbNailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public void savePost(@RequestPart("data") @Valid PhotoPostRequestDto photoPostRequestDto, @RequestPart("photo-file") List<MultipartFile> files,
                         @AuthenticationPrincipal UserDetails userDetails){

        photoPostService.savePost(photoPostRequestDto, files, userDetails.getUsername());
    }

    /*
    사진 게시판 목록(썸네일) 조회
     */
    @GetMapping("/photo-post")
    public ResponseEntity<List<ThumbNailDto>> showThumbNail(@RequestParam("offset") int offset, @RequestParam("limit") int limit){

        List<ThumbNailDto> thumbNailDtoList = thumbNailService.findAllThumbNail(offset, limit);

        return new ResponseEntity<>(thumbNailDtoList, HttpStatus.OK);
    }

    /*
    사진 게시물 조회
     */
    @GetMapping("/photo-post/{post_id}")
    public ResponseEntity<Map<String, Object>> showPost(@PathVariable("post_id") Long postId,
                                                        @AuthenticationPrincipal UserDetails userDetails){

        Map<String, Object> result = new HashMap<>();

        PhotoPostDto photoPostDto = photoPostService.findPostById(postId, userDetails.getUsername());
        List<AttachedFileDto> fileDtoList = attachedFileService.findAllFilesByPost(postId);
        List<CommentDto> commentDtoList = commentService.findCommentByPost(postId);
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(postId, userDetails.getUsername());

        result.put("post-data", photoPostDto);
        result.put("file-data", fileDtoList);
        result.put("comment-data", commentDtoList);
        result.put("is-member-liked", isMemberLiked);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

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
