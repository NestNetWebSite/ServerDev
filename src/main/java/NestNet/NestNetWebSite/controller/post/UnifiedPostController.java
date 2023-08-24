package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.dto.request.PostLikeRequest;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.dto.response.UnifiedPostResponse;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.UnifiedPostService;
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
public class UnifiedPostController {

    private final UnifiedPostService unifiedPostService;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    /*
    통합 게시판 게시물 저장
     */
    @PostMapping("unified-post/post")
    public void savePost(@RequestPart("data") @Valid UnifiedPostRequest unifiedPostRequest, @RequestPart("file") List<MultipartFile> files,
                         @AuthenticationPrincipal UserDetails userDetails){

        unifiedPostService.savePost(unifiedPostRequest, files, userDetails.getUsername());
    }

    /*
    통합 게시판 Type에 따른 목록 조회 (FREE, DEV, CAREER)
     */
    @GetMapping("unified-post")
    public ApiResult<?> showPostListByType(@RequestParam(value = "post-type") UnifiedPostType unifiedPostType,
                                        @RequestParam("offset") int offset, @RequestParam("limit") int limit){

        return unifiedPostService.findPostList(unifiedPostType, offset, limit);
    }

    /*
    통합 게시판 게시물 조회
     */
    @GetMapping("/unified-post/{post_id}")
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId,
                                                        @AuthenticationPrincipal UserDetails userDetails){

        Map<String, Object> result = new HashMap<>();

        UnifiedPostResponse postDto = unifiedPostService.findPostById(postId, userDetails.getUsername());
        List<AttachedFileResponse> fileDtoList = attachedFileService.findAllFilesByPost(postId);
        List<CommentResponse> commentResponseList = commentService.findCommentByPost(postId, userDetails.getUsername());
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(postId, userDetails.getUsername());

        result.put("post-data", postDto);
        result.put("file-data", fileDtoList);
        result.put("comment-data", commentResponseList);
        result.put("is-member-liked", isMemberLiked);

        return ApiResult.success(result);
    }

    /*
    통합 게시물 수정
     */

    /*
    좋아요 누름
     */
    @PostMapping("/exam-collection-post/like")
    public void like(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.saveLike(request.getPostId(), userDetails.getUsername());
        unifiedPostService.like(request.getPostId());
    }

    /*
    좋아요 취소
     */
    @PostMapping("/exam-collection-post/cancel-like")
    public void dislike(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.cancelLike(request.getPostId(), userDetails.getUsername());
        unifiedPostService.cancelLike(request.getPostId());
    }

}
