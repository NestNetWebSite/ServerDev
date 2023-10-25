package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequest;
import NestNet.NestNetWebSite.dto.request.PostLikeRequest;
import NestNet.NestNetWebSite.dto.response.*;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.ExamCollectionPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "시험 기출 게시글 API")
public class ExamCollectionPostController {

    private final ExamCollectionPostService examCollectionPostService;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    /*
    시험 기출 게시판 게시물 저장
     */
    @PostMapping("/exam-collection-post/post")
    @Operation(summary = "시험 기출 게시물 단건 저장", description = "")
    public ApiResult<?> savePost(@RequestPart("data") @Valid ExamCollectionPostRequest examCollectionPostRequest,
                                 @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                 @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){

        return examCollectionPostService.savePost(examCollectionPostRequest, files, userDetails.getUsername(), response);
    }

    /*
    시험 기출 게시판 목록 조희
     */
    @GetMapping("/exam-collection-post")
    public ApiResult<?> showPostListByFilter(@RequestParam(value = "subject", required = false) String subject,
                                          @RequestParam(value = "professor", required = false) String professor,
                                          @RequestParam(value = "year", required = false) Integer year,
                                          @RequestParam(value = "semester", required = false) Integer semester,
                                          @RequestParam(value = "examType", required = false) ExamType examType,
                                          @RequestParam("offset") int offset, @RequestParam("limit") int limit){

        return examCollectionPostService.findPostByFilter(subject, professor, year, semester, examType, offset, limit);
    }

    /*
    시험 기출 게시판 게시물 단건 조회
     */
    @GetMapping("/exam-collection-post/{post_id}")
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId,
                                                        @AuthenticationPrincipal UserDetails userDetails){

        Map<String, Object> result = new HashMap<>();

        ExamCollectionPostResponse postDto = examCollectionPostService.findPostById(postId, userDetails.getUsername());
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
    파일 다운로드
     */
    @GetMapping("/exam-collection-post/{postId}/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(value = "postId") Long postId, @PathVariable(value = "fileName") String fileName){

        InputStreamResource inputStreamResource = attachedFileService.findFile(postId, fileName);

        HttpHeaders headers = new HttpHeaders();
        //파일 다운로드 시 클라이언트에게 이진 파일임을 알림.
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // 파일 스트림을 ResponseEntity로 감싸서 전송
        return ResponseEntity.ok()
                .headers(headers)
                .body(inputStreamResource);
    }

    /*
    족보 게시물 수정
     */
    @PostMapping("/exam-collection-post/modify")
    public ApiResult<?> modifyPost(@RequestPart("data") ExamCollectionPostModifyRequest modifyRequest,
                                   @RequestPart(value = "file-id", required = false) List<Long> fileIdList,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                   HttpServletResponse response){

        if(fileIdList == null){
            fileIdList = new ArrayList<>();
        }
        if(files == null){
            files = new ArrayList<>();
        }

        examCollectionPostService.modifyPost(modifyRequest);
        boolean isCompleted = attachedFileService.modifyFiles(fileIdList, files, modifyRequest.getId());

        if(isCompleted) return ApiResult.success("게시물 수정 완료");
        else return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "파일 수정 에러");
    }

    /*
    족보 게시물 삭제 -> 게시물, 첨부파일, 댓글, 좋아요 모두 삭제
     */
    @DeleteMapping("/exam-collection-post/delete")
    public ApiResult<?> deletePost(@RequestParam(value = "postId") Long postId, HttpServletResponse response){

        boolean isComplete = attachedFileService.deleteFiles(postId);
        examCollectionPostService.deletePost(postId);
        commentService.deleteAllComments(postId);
        postLikeService.deleteLike(postId);

        if(isComplete){
            return ApiResult.success("게시물 삭제 완료");
        }
        else{
            return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "게시물 삭제 실패. 서버 에러");
        }
    }


    /*
    좋아요 누름
     */
    @PostMapping("/exam-collection-post/like")
    public void like(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.saveLike(request.getPostId(), userDetails.getUsername());
        examCollectionPostService.like(request.getPostId());
    }

    /*
    좋아요 취소
     */
    @PostMapping("/exam-collection-post/cancel-like")
    public void dislike(@RequestBody PostLikeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        postLikeService.cancelLike(request.getPostId(), userDetails.getUsername());
        examCollectionPostService.cancelLike(request.getPostId());
    }

}
