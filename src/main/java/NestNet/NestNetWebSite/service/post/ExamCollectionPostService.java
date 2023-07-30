package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequestDto;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostDto;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostListDto;
import NestNet.NestNetWebSite.repository.post.AttachedFileRepository;
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
    public void savePost(ExamCollectionPostRequestDto examCollectionPostRequestDto, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId);

        ExamCollectionPost post = examCollectionPostRequestDto.toEntity(member);

        List<AttachedFile> attachedFileList = new ArrayList<>();
        for(MultipartFile file : files){
            AttachedFile attachedFile = new AttachedFile(post, file);
            attachedFileList.add(new AttachedFile(post, file));
            post.addAttachedFiles(attachedFile);
        }

        examCollectionPostRepository.save(post);
        attachedFileRepository.saveAll(attachedFileList, files);
    }

    /*
    필터에 따른 족보 리스트 조희
     */
    public List<ExamCollectionPostListDto> findPostByFilter(String subject, String professor, Integer year, Integer semester, ExamType examType){

        List<ExamCollectionPost> posts = examCollectionPostRepository.findAllExamCollectionPostByFilter(subject, professor, year, semester, examType);

        List<ExamCollectionPostListDto> resultList = new ArrayList<>();
        for(ExamCollectionPost post : posts){
            resultList.add(
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

        return resultList;
    }

    /*
    족보 게시물 단건 상세 조회
     */
    public ExamCollectionPostDto findPostById(Long id, String memberLoginId){

        ExamCollectionPost post = examCollectionPostRepository.findById(id);
        Member member = memberRepository.findByLoginId(memberLoginId);
        List<AttachedFile> files = attachedFileRepository.findByPost(post);

        return ExamCollectionPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .bodyContent(post.getBodyContent())
                .subject(post.getSubject())
                .professor(post.getProfessor())
                .year(post.getYear())
                .semester(post.getSemester())
                .examType(post.getExamType())
                .userName(member.getName())
                .attachedFileList(files.stream().toList())
                .build();
    }
}
