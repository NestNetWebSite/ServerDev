package NestNet.NestNetWebSite.controller.comment;

import NestNet.NestNetWebSite.dto.request.CommentRequest;
import NestNet.NestNetWebSite.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    /*
    댓글 작성
     */
    @PostMapping("/comment/{post_id}")
    @Operation(summary = "댓글 작성", description = "")
    public void writeComment(@PathVariable("post_id") Long postId, @RequestBody CommentRequest commentRequest,
                                  @AuthenticationPrincipal UserDetails userDetails){

        commentService.saveComment(commentRequest, postId, userDetails.getUsername());
    }

    /*
    댓글 수정
     */
    @PostMapping("/comment/modify/{comment_id}")
    @Operation(summary = "댓글 수정", description = "로그인한 사용자가 자신이 작성한 댓글을 수정할 수 있다.")
    public void modifyComment(@PathVariable("comment_id") Long commentId, @RequestBody CommentRequest commentRequest){

        commentService.modifyComment(commentRequest, commentId);
    }

    /*
    댓글 삭제
     */
    @DeleteMapping("/comment/delete/{comment_id}")
    @Operation(summary = "댓글 삭제", description = "로그인한 사용자가 자신이 작성한 댓글을 삭제할 수 있다.")
    public void deleteComment(@PathVariable("comment_id") Long commentId){

        commentService.deleteComment(commentId);
    }
}
