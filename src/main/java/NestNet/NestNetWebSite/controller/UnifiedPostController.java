package NestNet.NestNetWebSite.controller;

import NestNet.NestNetWebSite.domain.post.PostType;
import NestNet.NestNetWebSite.dto.request.AttachedFileRequestDto;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequestDto;
import NestNet.NestNetWebSite.service.AttachedFileService;
import NestNet.NestNetWebSite.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UnifiedPostController {

    private final AttachedFileService attachedFileService;
    private final PostService PostService;

    /*
    통합 게시판 목록 get 요청 처리
     */
    @GetMapping("/unified-post/{post_type}")
    public void showPost(@PathVariable("post_type") PostType postType,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "limit", defaultValue = "10") int limit){

        PostService.findAllFromUnifiedPost(postType, offset, limit);
    }

    /*
    통합 게시판 (자유, 개발, 진로) post 요청 처리
     */
    @PostMapping("/unified-post/post")
    public void savePost(@RequestBody @Valid UnifiedPostRequestDto unifiedPostRequestDto,
                         @RequestBody @Valid List<AttachedFileRequestDto> attachedFileRequestDtos){

        attachedFileService.saveFiles(attachedFileRequestDtos);
        PostService.save(unifiedPostRequestDto);
    }



}
