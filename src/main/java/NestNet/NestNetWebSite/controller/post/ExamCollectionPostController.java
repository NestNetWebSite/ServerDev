package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequestDto;
import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostDto;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostListDto;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.ExamCollectionPostService;
import NestNet.NestNetWebSite.service.post.UnifiedPostService;
import NestNet.NestNetWebSite.service.member.MemberService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
public class ExamCollectionPostController {

    private final ExamCollectionPostService examCollectionPostService;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    /*
    족보 게시판 게시물 저장
     */
    @PostMapping("/exam-collection-post/post")
    public void savePost(@RequestPart("data") @Valid ExamCollectionPostRequestDto examCollectionPostRequestDto, @RequestPart("file") List<MultipartFile> files,
                         @AuthenticationPrincipal UserDetails userDetails){

        examCollectionPostService.savePost(examCollectionPostRequestDto, files, userDetails.getUsername());
    }

    /*
    족보 게시판 목록 조희
     */
    @GetMapping("/exam-collection-post")
    public ResponseEntity<List<ExamCollectionPostListDto>> showPostListByFilter(@RequestParam(value = "subject", required = false) String subject,
                                                                                @RequestParam(value = "professor", required = false) String professor,
                                                                                @RequestParam(value = "year", required = false) Integer year,
                                                                                @RequestParam(value = "semester", required = false) Integer semester,
                                                                                @RequestParam(value = "examType", required = false) ExamType examType){

        List<ExamCollectionPostListDto> resultList = examCollectionPostService.findPostByFilter(subject, professor, year, semester, examType);

        return new ResponseEntity<>(resultList, HttpStatus.OK);

    }

    /*
    족보 게시판 게시물 단건 조회
     */
    @GetMapping("exam-collection-post/{post_id}")
    public ResponseEntity<Map<String, Object>> showPost(@PathVariable("post_id") Long postId,
                                                        @AuthenticationPrincipal UserDetails userDetails){

        Map<String, Object> result = new HashMap<>();

        ExamCollectionPostDto postDto = examCollectionPostService.findPostById(postId, userDetails.getUsername());
        List<AttachedFileDto> fileDtoList = attachedFileService.findAllFilesByPost(postId);
        List<CommentDto> commentDtoList = commentService.findCommentByPost(postId);
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(postId, userDetails.getUsername());

        result.put("post-data", postDto);
        result.put("file-data", fileDtoList);
        result.put("comment-data", commentDtoList);
        result.put("is-member-liked", isMemberLiked);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /*
    좋아요 누름
     */
    @GetMapping("/exam-collection-post/{post_id}/like")
    public void like(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.saveLike(postId, userDetails.getUsername());
    }

    /*
    좋아요 취소
     */
    @GetMapping("/exam-collection-post/{post_id}/cancel_like")
    public void dislike(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.cancelLike(postId, userDetails.getUsername());
    }

}
