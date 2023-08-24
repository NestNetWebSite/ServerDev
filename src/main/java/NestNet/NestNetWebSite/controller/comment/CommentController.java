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

        System.out.println("====================댓글 작성 컨트롤러====================");

        commentService.saveComment(commentRequest, postId, userDetails.getUsername());
    }

    /*
    댓글 수정
     */
    @PostMapping("/comment/modify/{comment_id}")
    public void modifyComment(@PathVariable("comment_id") Long commentId,
                              @RequestBody CommentRequest commentRequest, @AuthenticationPrincipal UserDetails userDetails){

        System.out.println("====================댓글 수정 컨트롤러====================");

        System.out.println("수정 : " + commentRequest.getContent());

        commentService.modifyComment(commentRequest, commentId);
    }

    /*
    댓글 삭제
     */
    @DeleteMapping("/comment/delete/{comment_id}")
    public void deleteComment(@PathVariable("comment_id") Long commentId,@AuthenticationPrincipal UserDetails userDetails){

        System.out.println("====================댓글 삭제 컨트롤러====================");

        commentService.DeleteComment(commentId);
    }
}
