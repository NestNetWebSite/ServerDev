package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequest;
import NestNet.NestNetWebSite.dto.request.PostLikeRequest;
import NestNet.NestNetWebSite.dto.response.*;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostDto;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostListResponse;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostResponse;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.post.ExamCollectionPostService;
import NestNet.NestNetWebSite.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    /*
    시험 기출 게시판 게시물 저장
     */
    @PostMapping("/exam-collection-post/post")
    @Operation(summary = "시험 기출 게시물 단건 저장", description = "시험 기출 게시물을 저장한다. 파일 저장에 실패한 경우 500 에러를 반환한다.")
    public ApiResult<?> savePost(@RequestPart("data") @Valid ExamCollectionPostRequest examCollectionPostRequest,
                                 @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                 @AuthenticationPrincipal UserDetails userDetails){

        return examCollectionPostService.savePost(examCollectionPostRequest, files, userDetails.getUsername());
    }

    /*
    시험 기출 게시판 목록 조희
     */
    @GetMapping("/exam-collection-post")
    @Operation(summary = "시험 기출 게시판 게시물 목록 조회", description = "과목, 교수, 년도, 학기, 중간/기말을 필터링(필수 아님)할 수 있다.", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = ExamCollectionPostListResponse.class)))
    })
    public ApiResult<?> showPostListByFilter(@RequestParam(value = "subject", required = false) String subject,
                                          @RequestParam(value = "professor", required = false) String professor,
                                          @RequestParam(value = "year", required = false) Integer year,
                                          @RequestParam(value = "semester", required = false) Integer semester,
                                          @RequestParam(value = "examType", required = false) ExamType examType,
                                          @RequestParam("page") int page, @RequestParam("size") int size){

        return examCollectionPostService.findPostByFilter(subject, professor, year, semester, examType, page, size);
    }

    /*
    시험 기출 게시물 단건 상세 조회
     */
    @GetMapping("/exam-collection-post/{post_id}")
    @Operation(summary = "시험 기출 게시판 게시물 단건 상세 조회", description = "", responses = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = ExamCollectionPostResponse.class)))
    })
    public ApiResult<?> showPost(@PathVariable("post_id") Long postId, @AuthenticationPrincipal UserDetails userDetails){

        ExamCollectionPostResponse result = examCollectionPostService.findPostById(postId, userDetails.getUsername());

        return ApiResult.success(result);
    }

    /*
    시험 기출 게시판 게시물 수정
     */
    @PostMapping("/exam-collection-post/modify")
    @Operation(summary = "시험 기출 게시판 게시물 수정", description = "파일 저장에 문제가 생기는 경우 500 에러를 반환한다.")
    public ApiResult<?> modifyPost(@RequestPart("data") ExamCollectionPostModifyRequest modifyRequest,
                                   @RequestPart(value = "file-id", required = false) List<Long> fileIdList,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files){

        examCollectionPostService.modifyPost(modifyRequest, fileIdList, files);

        return ApiResult.success("게시물 수정 완료");
    }

}
