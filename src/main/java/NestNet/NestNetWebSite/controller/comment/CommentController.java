package NestNet.NestNetWebSite.controller.comment;

import NestNet.NestNetWebSite.dto.request.CommentRequestDto;
import NestNet.NestNetWebSite.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /*
    댓글 작성
     */
    @PostMapping("/comment/{post_id}")
    public void writeComment(@PathVariable("post_id") Long postId, @RequestBody CommentRequestDto commentRequestDto,
                             @AuthenticationPrincipal UserDetails userDetails){

        commentService.saveComment(commentRequestDto, postId, userDetails.getUsername());
    }
}
