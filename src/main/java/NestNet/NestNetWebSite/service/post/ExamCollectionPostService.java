package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.domain.token.dto.request.ExamCollectionPostRequest;
import NestNet.NestNetWebSite.domain.token.dto.response.ExamCollectionPostResponse;
import NestNet.NestNetWebSite.domain.token.dto.response.ExamCollectionPostListResponse;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.ExamCollectionPostRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
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
    private final AttachedFileRepository attachedFileRepository;
    private final MemberRepository memberRepository;

    /*
    족보 게시판에 게시물 저장
     */
    @Transactional
    public void savePost(ExamCollectionPostRequest examCollectionPostRequest, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId);

        ExamCollectionPost post = examCollectionPostRequest.toEntity(member);

        List<AttachedFile> attachedFileList = new ArrayList<>();
        for(MultipartFile file : files){
            AttachedFile attachedFile = new AttachedFile(post, file);
            attachedFileList.add(attachedFile);
            post.addAttachedFiles(attachedFile);
        }

        examCollectionPostRepository.save(post);
        attachedFileRepository.saveAll(attachedFileList, files);
    }

    /*
    필터에 따른 족보 리스트 조희
     */
    public ApiResult<?> findPostByFilter(String subject, String professor, Integer year, Integer semester, ExamType examType, int offset, int limit){

        List<ExamCollectionPost> posts = examCollectionPostRepository.findAllExamCollectionPostByFilter(subject, professor, year, semester, examType, offset, limit);

        List<ExamCollectionPostListResponse> resultList = new ArrayList<>();
        for(ExamCollectionPost post : posts){
            resultList.add(
                    ExamCollectionPostListResponse.builder()
                            .id(post.getId())
                            .subject(post.getSubject())
                            .professor(post.getProfessor())
                            .year(post.getYear())
                            .semester(post.getSemester())
                            .examType(post.getExamType())
                            .userName(post.getMember().getName())
                            .build());
        }

        return ApiResult.success(resultList);
    }

    /*
    족보 게시물 단건 상세 조회
     */
    @Transactional
    public ExamCollectionPostResponse findPostById(Long id, String memberLoginId){

        ExamCollectionPost post = examCollectionPostRepository.findById(id);
        examCollectionPostRepository.addViewCount(post, memberLoginId);

        return ExamCollectionPostResponse.builder()
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
                .build();
    }

    /*
    좋아요
     */
    @Transactional
    public void like(Long id){

        ExamCollectionPost post = examCollectionPostRepository.findById(id);
        examCollectionPostRepository.like(post);
    }

    /*
    좋아요 취소
     */
    @Transactional
    public void cancelLike(Long id){

        ExamCollectionPost post = examCollectionPostRepository.findById(id);
        examCollectionPostRepository.cancelLike(post);
    }

    /*
    족보 게시물 삭제
     */
    public void deletePost(Long id){
        examCollectionPostRepository.deletePost(id);
    }
}
