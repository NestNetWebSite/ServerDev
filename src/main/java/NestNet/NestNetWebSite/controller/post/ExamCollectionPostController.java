package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.dto.request.AttachedFileRequestDto;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequestDto;
import NestNet.NestNetWebSite.service.post.ExamCollectionPostService;
import NestNet.NestNetWebSite.service.post.UnifiedPostService;
import NestNet.NestNetWebSite.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExamCollectionPostController {

    private final ExamCollectionPostService examCollectionPostService;
    private final UnifiedPostService unifiedPostService;
    private final MemberService memberService;

    /*
    족보 게시판 목록 조희
     */
//    @GetMapping("/exam-collection-post")
//    public List<ExamCollectionPostListDto> showPostList(@RequestParam(value = "offset", defaultValue = "0") int offset,
//                         @RequestParam(value = "limit", defaultValue = "10") int limit){
//
//        return unifiedPostService.findListFromExamCollectionPost(offset, limit);
//    }

    /*
    족보 게시판 게시물 단건 조회
     */
//    @GetMapping("exam-collection-post/{post_id}")
//    public ExamCollectionPostDto showPost(@PathVariable("post_id") Long postId){
//        ExamCollectionPostDto postDto = postService.findPostFromExamCollectionPost(postId);
//        List<AttachedFileDto> fileDto = attachedFileService.findFilesByPost(postId);
//
//    }

    /*
    족보 게시판 게시물 저장
     */
    @PostMapping("/exam-collection-post/post")
    public void savePost(@RequestPart("data") @Valid ExamCollectionPostRequestDto examCollectionPostRequestDto, @RequestPart("attachedFile") List<MultipartFile> files,
                         @AuthenticationPrincipal UserDetails userDetails){

        System.out.println(userDetails.getUsername());
        System.out.println(examCollectionPostRequestDto.getSubject());

        examCollectionPostService.savePost(examCollectionPostRequestDto, files, userDetails.getUsername());

    }


}
