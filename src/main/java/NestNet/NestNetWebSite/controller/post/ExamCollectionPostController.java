package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostResponse;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.ExamCollectionPostService;
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
public class ExamCollectionPostController {

    private final ExamCollectionPostService examCollectionPostService;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    /*
    족보 게시판 게시물 저장
     */
    @PostMapping("/exam-collection-post/post")
    public void savePost(@RequestPart("data") @Valid ExamCollectionPostRequest examCollectionPostRequest, @RequestPart("file") List<MultipartFile> files,
                         @AuthenticationPrincipal UserDetails userDetails){

        examCollectionPostService.savePost(examCollectionPostRequest, files, userDetails.getUsername());
    }

    /*
    족보 게시판 목록 조희
     */
    @GetMapping("/exam-collection-post")
    public ApiResult<?> showPostListByFilter(@RequestParam(value = "subject", required = false) String subject,
                                          @RequestParam(value = "professor", required = false) String professor,
                                          @RequestParam(value = "year", required = false) Integer year,
                                          @RequestParam(value = "semester", required = false) Integer semester,
                                          @RequestParam(value = "examType", required = false) ExamType examType){

        return examCollectionPostService.findPostByFilter(subject, professor, year, semester, examType);
    }

    /*
    족보 게시판 게시물 단건 조회
     */
    @GetMapping("exam-collection-post/{post_id}")
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId,
                                                        @AuthenticationPrincipal UserDetails userDetails){

        Map<String, Object> result = new HashMap<>();

        ExamCollectionPostResponse postDto = examCollectionPostService.findPostById(postId, userDetails.getUsername());
        List<AttachedFileResponse> fileDtoList = attachedFileService.findAllFilesByPost(postId);
        List<CommentResponse> commentResponseList = commentService.findCommentByPost(postId);
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(postId, userDetails.getUsername());

        result.put("post-data", postDto);
        result.put("file-data", fileDtoList);
        result.put("comment-data", commentResponseList);
        result.put("is-member-liked", isMemberLiked);

        return ApiResult.success(result);
    }

    /*
    좋아요 누름
     */
    @GetMapping("/exam-collection-post/{post_id}/like")
    public void like(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.saveLike(postId, userDetails.getUsername());
        examCollectionPostService.like(postId);
    }

    /*
    좋아요 취소
     */
    @GetMapping("/exam-collection-post/{post_id}/cancel_like")
    public void dislike(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.cancelLike(postId, userDetails.getUsername());
        examCollectionPostService.cancelLike(postId);
    }

}
