package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.introduction.IntroductionPost;
import NestNet.NestNetWebSite.dto.request.IntroductionPostRequest;
import NestNet.NestNetWebSite.dto.response.introductionpost.IntroductionPostListDto;
import NestNet.NestNetWebSite.dto.response.introductionpost.IntroductionPostListResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.IntroductionPostRepository;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.photofile.PhotoFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IntroductionPostService {

    private final IntroductionPostRepository introductionPostRepository;
    private final AttachedFileService attachedFileService;
    private final MemberRepository memberRepository;

    /*
    자기소개 게시판에 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(IntroductionPostRequest introductionPostRequest, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        IntroductionPost introductionPost = introductionPostRequest.toEntity(member);

        introductionPostRepository.save(introductionPost);

        if(files != null){
            attachedFileService.save(introductionPost, files);
        }

        return ApiResult.success("게시물 저장 성공");
    }

    /*
    자기 소개 게시판 게시물 리스트 조회
     */
//    public ApiResult<?> findPostListByPaging(int page, int size){
//
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
//
//        Page<IntroductionPost> postPage = introductionPostRepository.findAll(pageRequest);
//
//        List<IntroductionPost> introductionPostList = postPage.getContent();
//
//        List<IntroductionPostListDto> dtoList = new ArrayList<>();
//        for(IntroductionPost post : introductionPostList){
//            dtoList.add(new IntroductionPostListDto(
//                    post.getId(), post.getTitle(), post.getViewCount(), post.getLikeCount(), post.getCreatedTime()),
//                    );
//        }
//
//        List<AttachedFile> attachedFileList = attachedFileService.fin
//
//        IntroductionPostListResponse result = new IntroductionPostListResponse(postPage.getTotalElements(), dtoList, )
//
//
//    }


    /*
    자기 소개 게시판 게시물 단건 조회
     */

}
