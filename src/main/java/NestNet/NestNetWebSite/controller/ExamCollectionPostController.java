package NestNet.NestNetWebSite.controller;

import NestNet.NestNetWebSite.dto.request.AttachedFileRequestDto;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequestDto;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostListDto;
import NestNet.NestNetWebSite.service.AttachedFileService;
import NestNet.NestNetWebSite.service.PostService;
import NestNet.NestNetWebSite.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExamCollectionPostController {

    private final AttachedFileService attachedFileService;
    private final PostService postService;
    private final MemberService memberService;

    /*
    족보 게시판 목록 조희
     */
    @GetMapping("/exam-collection-post")
    public List<ExamCollectionPostListDto> showPostList(@RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "limit", defaultValue = "10") int limit){

        return postService.findListFromExamCollectionPost(offset, limit);
    }

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
    족보 게시판 post 요청 처리
     */
    @PostMapping("/exam-collection-post/post")
    public void savePost(@RequestBody @Valid ExamCollectionPostRequestDto examCollectionPostRequestDto,
                         @RequestBody @Valid List<AttachedFileRequestDto> attachedFileRequestDtos){

        attachedFileService.saveFiles(attachedFileRequestDtos);
        postService.save(examCollectionPostRequestDto);
    }


}
