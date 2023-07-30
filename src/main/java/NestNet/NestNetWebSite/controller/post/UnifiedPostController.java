package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.service.post.UnifiedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UnifiedPostController {

    private final UnifiedPostService UnifiedPostService;

    /*
    통합 게시판 목록 get 요청 처리
     */
    @GetMapping("/unified-post/{post_type}")
    public void showPost(@PathVariable("post_type") UnifiedPostType unifiedPostType,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "limit", defaultValue = "10") int limit){

        UnifiedPostService.findAllFromUnifiedPost(unifiedPostType, offset, limit);
    }

    /*
    통합 게시판 (자유, 개발, 진로) post 요청 처리
     */
//    @PostMapping("/unified-post/post")
//    public void savePost(@RequestBody @Valid UnifiedPostRequestDto unifiedPostRequestDto,
//                         @RequestBody @Valid List<AttachedFileRequestDto> attachedFileRequestDtos){
//
//        attachedFileService.saveFiles(attachedFileRequestDtos);
//        UnifiedPostService.save(unifiedPostRequestDto);
//    }



}
