package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPost;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.dto.request.UnifiedPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequest;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostDto;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostListDto;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostListResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.UnifiedPostRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnifiedPostService {

    private final UnifiedPostRepository unifiedPostRepository;
    private final MemberRepository memberRepository;
    private final AttachedFileService attachedFileService;
    private final PostService postService;

    /*
    통합 게시판 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(UnifiedPostRequest unifiedPostRequest, List<MultipartFile> files, String memberLoginId) {

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        UnifiedPost post = unifiedPostRequest.toEntity(member);

        unifiedPostRepository.save(post);

        attachedFileService.save(post, files);

        return ApiResult.success("게시물 저장 성공");
    }

    /*
    통합 게시판 목록 조회
     */
    public ApiResult<?> findPostList(UnifiedPostType unifiedPostType, int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<UnifiedPost> unifiedPostPage = unifiedPostRepository.findByUnifiedPostTypeByPaging(unifiedPostType, pageRequest);

        List<UnifiedPost> postList = unifiedPostPage.getContent();

        List<UnifiedPostListDto> dtoList = new ArrayList<>();
        for(UnifiedPost post : postList){
            dtoList.add(new UnifiedPostListDto(post.getMember().getName(), post.getTitle(),
                    post.getCreatedTime(), post.getViewCount(), post.getLikeCount()));
        }

        UnifiedPostListResponse result = new UnifiedPostListResponse(unifiedPostPage.getTotalElements(), dtoList);

        return ApiResult.success(result);
    }

    /*
    통합 게시판 게시물 단건 조회
     */
    @Transactional
    public UnifiedPostDto findPostById(Long id, String memberLoginId){

        UnifiedPost post = unifiedPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        postService.addViewCount(post, memberLoginId);

        if(memberLoginId.equals(post.getMember().getLoginId())){
            return UnifiedPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .unifiedPostType(post.getUnifiedPostType())
                    .userName(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(true)
                    .build();
        }
        else{
            return UnifiedPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .unifiedPostType(post.getUnifiedPostType())
                    .userName(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(true)
                    .build();
        }
    }

    /*
    통합 게시물 수정
     */
    @Transactional
    public void modifyPost(UnifiedPostModifyRequest request){

        UnifiedPost post = unifiedPostRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 변경 감지 -> 자동 update
        post.modifyPost(request.getTitle(), request.getBodyContent(), request.getUnifiedPostType());
    }
}
