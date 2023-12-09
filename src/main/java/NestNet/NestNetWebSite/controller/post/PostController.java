package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.PostLikeRequest;
import NestNet.NestNetWebSite.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시판 공통 기능 API")
public class PostController {

    private final PostService postService;

    /*
    게시물 삭제 -> 게시물, 첨부파일, 댓글, 좋아요 모두 삭제
     */
    @DeleteMapping("/post/delete")
    @Operation(summary = "게시물 삭제", description = "파일 삭제에 문제가 생기는 경우 500 에러를 반환한다.")
    public ApiResult<?> deletePost(@RequestParam(value = "postId") Long postId){

        postService.deletePost(postId);

        return ApiResult.success("게시물 삭제 완료");
    }


    /*
    좋아요 누름
     */
    @PostMapping("/post/like")
    @Operation(summary = "좋아요", description = "")
    public void like(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postService.like(request.getPostId(), userDetails.getUsername());
    }

    /*
    좋아요 취소
     */
    @PostMapping("/post/cancel-like")
    @Operation(summary = "좋아요 취소", description = "")
    public void dislike(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postService.cancelLike(request.getPostId(), userDetails.getUsername());
    }
}
