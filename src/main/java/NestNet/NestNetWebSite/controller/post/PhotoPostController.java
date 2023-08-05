package NestNet.NestNetWebSite.controller.post;

import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequestDto;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequestDto;
import NestNet.NestNetWebSite.service.post.PhotoPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PhotoPostController {

    private final PhotoPostService photoPostService;

    /*
    사진 게시판 게시물 저장
     */
    @PostMapping("/photo-post/post")
    public void savePost(@RequestPart("data") @Valid PhotoPostRequestDto photoPostRequestDto, @RequestPart("photo-file") List<MultipartFile> files,
                         @AuthenticationPrincipal UserDetails userDetails){

        photoPostService.savePost(photoPostRequestDto, files, userDetails.getUsername());
    }


}
