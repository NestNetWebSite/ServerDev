package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPost;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.dto.request.UnifiedPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequest;
import NestNet.NestNetWebSite.dto.response.unified.UnifiedPostDto;
import NestNet.NestNetWebSite.dto.response.unified.UnifiedPostListDto;
import NestNet.NestNetWebSite.dto.response.unified.UnifiedPostResponse;
import NestNet.NestNetWebSite.dto.response.unified.UnifiedPostListResponse;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.UnifiedPostRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnifiedPostService {

    private final UnifiedPostRepository unifiedPostRepository;
    private final MemberRepository memberRepository;
    private final AttachedFileRepository attachedFileRepository;

    /*
    통합 게시판 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(UnifiedPostRequest unifiedPostRequest, List<MultipartFile> files, String memberLoginId, HttpServletResponse response) {

        Member member = memberRepository.findByLoginId(memberLoginId);

        UnifiedPost post = unifiedPostRequest.toEntity(member);

        if(files != null){
            List<AttachedFile> attachedFileList = new ArrayList<>();

            for(MultipartFile file : files){
                AttachedFile attachedFile = new AttachedFile(post, file);
                attachedFileList.add(attachedFile);
                post.addAttachedFiles(attachedFile);
            }
            boolean isFileSaved = attachedFileRepository.saveAll(attachedFileList, files);

            if(isFileSaved == false){
                return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 실패");
            }
        }

        unifiedPostRepository.save(post);

        return ApiResult.success("게시물 저장 성공");
    }

    /*
    통합 게시판 목록 조회
     */
    public ApiResult<?> findPostList(UnifiedPostType unifiedPostType, int offset, int limit){

        List<UnifiedPost> postList = unifiedPostRepository.findUnifiedPostByType(offset, limit, unifiedPostType);
        Long size = unifiedPostRepository.findTotalSize(unifiedPostType);

        List<UnifiedPostListDto> dtoList = new ArrayList<>();
        for(UnifiedPost post : postList){
            dtoList.add(new UnifiedPostListDto(post.getMember().getName(), post.getTitle(),
                    post.getCreatedTime(), post.getViewCount(), post.getLikeCount()));
        }

        UnifiedPostListResponse result = new UnifiedPostListResponse(size, dtoList);

        return ApiResult.success(result);
    }

    /*
    통합 게시판 게시물 단건 조회
     */
    @Transactional
    public UnifiedPostDto findPostById(Long id, String memberLoginId){

        UnifiedPost post = unifiedPostRepository.findById(id);
        unifiedPostRepository.addViewCount(post, memberLoginId);

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
    좋아요
     */
    @Transactional
    public void like(Long id){

        UnifiedPost post = unifiedPostRepository.findById(id);
        unifiedPostRepository.like(post);
    }

    /*
    좋아요 취소
     */
    @Transactional
    public void cancelLike(Long id){

        UnifiedPost post = unifiedPostRepository.findById(id);
        unifiedPostRepository.cancelLike(post);
    }

    /*
    통합 게시물 수정
     */
    @Transactional
    public void modifyPost(UnifiedPostModifyRequest request){

        UnifiedPost post = unifiedPostRepository.findById(request.getId());

        // 변경 감지 -> 자동 update
        post.modifyPost(request.getTitle(), request.getBodyContent(), request.getUnifiedPostType());
    }

    /*
    통합 게시물 삭제
     */
    @Transactional
    public void deletePost(Long postId){

        Post post = unifiedPostRepository.findById(postId);

        unifiedPostRepository.deletePost(post);
    }
}
