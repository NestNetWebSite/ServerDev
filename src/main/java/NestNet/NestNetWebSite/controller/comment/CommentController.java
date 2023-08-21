package NestNet.NestNetWebSite.controller.comment;

import NestNet.NestNetWebSite.dto.request.CommentRequest;
import NestNet.NestNetWebSite.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    /*
    댓글 작성
     */
    @PostMapping("/comment/{post_id}")
    public void writeComment(@PathVariable("post_id") Long postId, @RequestBody CommentRequest commentRequest,
                                  @AuthenticationPrincipal UserDetails userDetails){

        commentService.saveComment(commentRequest, postId, userDetails.getUsername());
    }

    /*
    댓글 수정
     */
    @PostMapping("/commnet/modify/{comment_id}")
    public void modifyComment(@PathVariable("comment_id") Long commentId,
                              @RequestBody CommentRequest commentRequest, @AuthenticationPrincipal UserDetails userDetails){

        commentService.modifyComment(commentRequest, commentId);
    }

}
