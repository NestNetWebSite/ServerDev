package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.comment.Comment;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPost;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.dto.request.UnifiedPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostDto;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostListDto;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostListResponse;
import NestNet.NestNetWebSite.dto.response.unifiedpost.UnifiedPostResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.post.UnifiedPostRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
public class UnifiedPostService {

    private final UnifiedPostRepository unifiedPostRepository;
    private final MemberRepository memberRepository;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
    private final PostService postService;

    /*
    통합 게시판 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(UnifiedPostRequest unifiedPostRequest, List<MultipartFile> files,
                                 String memberLoginId) {

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        UnifiedPost post = unifiedPostRequest.toEntity(member);

        unifiedPostRepository.save(post);

        if(!ObjectUtils.isEmpty(files)){
            List<AttachedFile> savedFileList = attachedFileService.save(post, files);

            // 양방향 연관관계 주입
            for(AttachedFile attachedFile : savedFileList){
                post.addAttachedFile(attachedFile);
            }
        }

        postService.addViewCount(post, member.getId());

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
            dtoList.add(new UnifiedPostListDto(post.getId(), post.getMember().getName(), post.getTitle(),
                    post.getCreatedTime(), post.getViewCount(), post.getLikeCount()));
        }

        UnifiedPostListResponse result = new UnifiedPostListResponse(unifiedPostPage.getTotalElements(), dtoList);

        return ApiResult.success(result);
    }

    /*
    통합 게시판 게시물 단건 조회
     */
    @Transactional
    public UnifiedPostResponse findPostById(Long id, String memberLoginId){

        Member loginMember = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        UnifiedPost post = unifiedPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<AttachedFile> attachedFileList = post.getAttachedFileList();

        List<Comment> commentList = post.getCommentList();

        UnifiedPostDto postDto = null;
        List<AttachedFileDto> fileDtoList = new ArrayList<>();
        List<CommentDto> commentDtoList = new ArrayList<>();

        for(AttachedFile attachedFile : attachedFileList){
            fileDtoList.add(new AttachedFileDto(attachedFile.getId(), attachedFile.getOriginalFileName(),
                    attachedFile.getSaveFilePath(), attachedFile.getSaveFileName()));
        }

        for(Comment comment : commentList){
            if(loginMember.getId() == comment.getMember().getId()){
                commentDtoList.add(new CommentDto(comment.getId(), comment.getMember().getLoginId(), comment.getMember().getName(), comment.getMember().getMemberAuthority(),
                        comment.getContent(), comment.getCreatedTime(), comment.getModifiedTime(), true));
            }
            else{
                commentDtoList.add(new CommentDto(comment.getId(), comment.getMember().getLoginId(), comment.getMember().getName(), comment.getMember().getMemberAuthority(),
                        comment.getContent(), comment.getCreatedTime(), comment.getModifiedTime(), false));
            }
        }

        if(loginMember.getId() == post.getMember().getId()){
            postDto = UnifiedPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .unifiedPostType(post.getUnifiedPostType())
                    .memberLoginId(post.getMember().getLoginId())
                    .username(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(true)
                    .build();
        }
        else{
            postDto = UnifiedPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .unifiedPostType(post.getUnifiedPostType())
                    .memberLoginId(post.getMember().getLoginId())
                    .username(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(false)
                    .build();
        }

        boolean isMemberLiked = postLikeService.isMemberLikedByPost(post, loginMember);

        postService.addViewCount(post, loginMember.getId());

        return new UnifiedPostResponse(postDto, fileDtoList, commentDtoList, isMemberLiked);
    }

    /*
    통합 게시물 수정
     */
    @Transactional
    public void modifyPost(UnifiedPostModifyRequest request, List<Long> fileIdList, List<MultipartFile> files){

        UnifiedPost post = unifiedPostRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        attachedFileService.modifyFiles(post, fileIdList, files);

        // 변경 감지 -> 자동 update
        post.modifyPost(request.getTitle(), request.getBodyContent(), request.getUnifiedPostType());
    }
}
