package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.introduction.IntroductionPost;
import NestNet.NestNetWebSite.dto.request.IntroductionPostRequest;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.IntroductionPostRepository;
import NestNet.NestNetWebSite.service.photofile.PhotoFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IntroductionPostService {

    private final IntroductionPostRepository introductionPostRepository;
    private final PhotoFileService photoFileService;
    private final MemberRepository memberRepository;

    /*
    자기소개 게시판에 게시물 저장
     */
    public ApiResult<?> savePost(IntroductionPostRequest introductionPostRequest, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        IntroductionPost introductionPost = introductionPostRequest.toEntity(member);

        introductionPostRepository.save(introductionPost);

        if(files != null){
            photoFileService.save(introductionPost, files);
        }

        return ApiResult.success("게시물 저장 성공");
    }


}
