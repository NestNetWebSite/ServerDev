package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.comment.Comment;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.introduction.IntroductionPost;
import NestNet.NestNetWebSite.dto.request.IntroductionPostRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import NestNet.NestNetWebSite.dto.response.introductionpost.IntroductionPostDto;
import NestNet.NestNetWebSite.dto.response.introductionpost.IntroductionPostListDto;
import NestNet.NestNetWebSite.dto.response.introductionpost.IntroductionPostListResponse;
import NestNet.NestNetWebSite.dto.response.introductionpost.IntroductionPostResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.IntroductionPostRepository;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
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
public class IntroductionPostService {

    private final IntroductionPostRepository introductionPostRepository;
    private final AttachedFileService attachedFileService;
    private final MemberRepository memberRepository;
    private final PostLikeService postLikeService;
    private final PostService postService;

    /*
    자기소개 게시판에 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(IntroductionPostRequest introductionPostRequest, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        IntroductionPost post = introductionPostRequest.toEntity(member);

        introductionPostRepository.save(post);

        if(!ObjectUtils.isEmpty(files)){
            List<AttachedFile> savedFileList = attachedFileService.save(post, files);

            // 양방향 연관관계 주입
            for(AttachedFile attachedFile : savedFileList){
                post.addAttachedFile(attachedFile);
            }
        }

        return ApiResult.success("게시물 저장 성공");
    }

    /*
    자기 소개 게시판 게시물 리스트 조회
     */
    public ApiResult<?> findPostListByPaging(int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<IntroductionPost> postPage = introductionPostRepository.findPostList(pageRequest);

        List<IntroductionPost> introductionPostList = postPage.getContent();

        List<IntroductionPostListDto> dtoList = new ArrayList<>();
        for(IntroductionPost post : introductionPostList){
            for(AttachedFile attachedFile : post.getAttachedFileList()){
                dtoList.add(new IntroductionPostListDto(
                        post.getId(), post.getTitle(), post.getViewCount(), post.getLikeCount(), post.getCreatedTime(),
                        attachedFile.getSaveFilePath(), attachedFile.getSaveFileName()));
            }
        }

        IntroductionPostListResponse result = new IntroductionPostListResponse(postPage.getTotalElements(), dtoList);

        return ApiResult.success(result);
    }


    /*
    자기 소개 게시판 게시물 단건 조회
     */
    @Transactional
    public IntroductionPostResponse findPostById(Long id, String memberLoginId){

        Member loginMember = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        IntroductionPost post = introductionPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        List<AttachedFile> attachedFileList = post.getAttachedFileList();

        List<Comment> commentList = post.getCommentList();

        List<AttachedFileDto> fileDtoList = new ArrayList<>();
        List<CommentDto> commentDtoList = new ArrayList<>();
        IntroductionPostDto postDto = null;

        for(AttachedFile attachedFile : attachedFileList){
            fileDtoList.add(new AttachedFileDto(attachedFile.getId(), attachedFile.getOriginalFileName(),
                    attachedFile.getSaveFilePath(), attachedFile.getSaveFileName()));
        }

        for(Comment comment : commentList){
            if(loginMember.getId() == comment.getMember().getId()){
                commentDtoList.add(new CommentDto(comment.getId(), comment.getMember().getName(), comment.getContent(),
                        comment.getCreatedTime(), comment.getModifiedTime(), true));
            }
            else{
                commentDtoList.add(new CommentDto(comment.getId(), comment.getMember().getName(), comment.getContent(),
                        comment.getCreatedTime(), comment.getModifiedTime(), false));
            }
        }

        if(loginMember.getId() == post.getMember().getId()){
            postDto = IntroductionPostDto.builder()
                    .id(post.getId())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .username(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(true)
                    .build();
        }
        else{
            postDto = IntroductionPostDto.builder()
                    .id(post.getId())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .username(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(false)
                    .build();
        }

        boolean isMemberLiked = postLikeService.isMemberLikedByPost(post, loginMember);

        postService.addViewCount(post, loginMember.getId());

        return new IntroductionPostResponse(postDto, fileDtoList, commentDtoList, isMemberLiked);
    }

}
