package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.comment.Comment;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostDto;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostListDto;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostResponse;
import NestNet.NestNetWebSite.dto.response.examcollectionpost.ExamCollectionPostListResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.post.ExamCollectionPostRepository;
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
public class ExamCollectionPostService {

    private final ExamCollectionPostRepository examCollectionPostRepository;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
    private final PostService postService;
    private final MemberRepository memberRepository;

    /*
    족보 게시판에 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(ExamCollectionPostRequest examCollectionPostRequest, List<MultipartFile> files,
                         String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        ExamCollectionPost post = examCollectionPostRequest.toEntity(member);

        examCollectionPostRepository.save(post);

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
    필터에 따른 족보 리스트 조희
     */
    public ApiResult<?> findPostByFilter(String subject, String professor, Integer year, Integer semester, ExamType examType, int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<ExamCollectionPost> postPage = examCollectionPostRepository.findAllByFilter(subject, professor, year, semester, examType, pageRequest);

        List<ExamCollectionPost> examCollectionPostList = postPage.getContent();

        List<ExamCollectionPostListDto> dtoList = new ArrayList<>();
        for(ExamCollectionPost post : examCollectionPostList){
            dtoList.add(
                    ExamCollectionPostListDto.builder()
                            .id(post.getId())
                            .subject(post.getSubject())
                            .professor(post.getProfessor())
                            .year(post.getYear())
                            .semester(post.getSemester())
                            .examType(post.getExamType())
                            .userName(post.getMember().getName())
                            .build());
        }

        ExamCollectionPostListResponse result = new ExamCollectionPostListResponse(postPage.getTotalElements(), dtoList);

        return ApiResult.success(result);
    }

    /*
    족보 게시물 단건 상세 조회
     */
    @Transactional
    public ExamCollectionPostResponse findPostById(Long id, String memberLoginId){

        Member loginMember = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        ExamCollectionPost post = examCollectionPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<AttachedFile> attachedFileList = post.getAttachedFileList();

        List<Comment> commentList = post.getCommentList();

        List<AttachedFileDto> fileDtoList = new ArrayList<>();
        List<CommentDto> commentDtoList = new ArrayList<>();
        ExamCollectionPostDto postDto = null;

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
            postDto = ExamCollectionPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .subject(post.getSubject())
                    .professor(post.getProfessor())
                    .year(post.getYear())
                    .semester(post.getSemester())
                    .examType(post.getExamType())
                    .userName(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(true)
                    .build();
        }
        else{
            postDto = ExamCollectionPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .subject(post.getSubject())
                    .professor(post.getProfessor())
                    .year(post.getYear())
                    .semester(post.getSemester())
                    .examType(post.getExamType())
                    .userName(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(false)
                    .build();
        }

        boolean isMemberLiked = postLikeService.isMemberLikedByPost(post, loginMember);

        postService.addViewCount(post, loginMember.getId());

        return new ExamCollectionPostResponse(postDto, fileDtoList, commentDtoList, isMemberLiked);
    }

    /*
    족보 게시물 수정
     */
    @Transactional
    public void modifyPost(ExamCollectionPostModifyRequest modifyRequest, List<Long> fileIdList, List<MultipartFile> files){

        ExamCollectionPost post = examCollectionPostRepository.findById(modifyRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        attachedFileService.modifyFiles(post, fileIdList, files);

        // 변경 감지 -> 자동 update
        post.modifyPost(modifyRequest.getTitle(), modifyRequest.getBodyContent(), modifyRequest.getSubject(),
                modifyRequest.getProfessor(), modifyRequest.getYear(), modifyRequest.getSemester(), modifyRequest.getExamType());
    }

}
