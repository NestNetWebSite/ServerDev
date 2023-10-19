package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequest;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostResponse;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostListResponse;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.ExamCollectionPostRepository;
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
public class ExamCollectionPostService {

    private final ExamCollectionPostRepository examCollectionPostRepository;
    private final AttachedFileRepository attachedFileRepository;
    private final MemberRepository memberRepository;

    /*
    족보 게시판에 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(ExamCollectionPostRequest examCollectionPostRequest, List<MultipartFile> files,
                         String memberLoginId, HttpServletResponse response){

        Member member = memberRepository.findByLoginId(memberLoginId);

        ExamCollectionPost post = examCollectionPostRequest.toEntity(member);

        examCollectionPostRepository.save(post);

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

        return ApiResult.success("게시물 저장 성공");
    }

    /*
    필터에 따른 족보 리스트 조희
     */
    public ApiResult<?> findPostByFilter(String subject, String professor, Integer year, Integer semester, ExamType examType, int offset, int limit){

        List<ExamCollectionPost> posts = examCollectionPostRepository.findAllExamCollectionPostByFilter(subject, professor, year, semester, examType, offset, limit);
        Long size = examCollectionPostRepository.findTotalSize();       //전체 족보 게시물 갯수

        Map<Object, Object> result = new HashMap<>();

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

        result.put("totalSize", size);
        result.put("posts", resultList);

        return ApiResult.success(result);
    }

    /*
    족보 게시물 단건 상세 조회
     */
    @Transactional
    public ExamCollectionPostResponse findPostById(Long id, String memberLoginId){

        ExamCollectionPost post = examCollectionPostRepository.findById(id);
        examCollectionPostRepository.addViewCount(post, memberLoginId);

        if(memberLoginId.equals(post.getMember().getLoginId())){
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
                    .isMemberWritten(true)
                    .build();
        }
        else{
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
                    .isMemberWritten(false)
                    .build();
        }
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
    족보 게시물 수정
     */
    @Transactional
    public void modifyPost(ExamCollectionPostModifyRequest modifyRequest){

        ExamCollectionPost post = examCollectionPostRepository.findById(modifyRequest.getId());

        // 변경 감지 -> 자동 update
        post.modifyPost(modifyRequest.getTitle(), modifyRequest.getBodyContent(), modifyRequest.getSubject(),
                modifyRequest.getProfessor(), modifyRequest.getYear(), modifyRequest.getSemester(), modifyRequest.getExamType());
    }

    /*
    족보 게시물 삭제
     */
    @Transactional
    public void deletePost(Long id){

        Post post = examCollectionPostRepository.findById(id);

        examCollectionPostRepository.deletePost(post);
    }
}
