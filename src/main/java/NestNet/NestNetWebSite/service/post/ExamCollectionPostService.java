package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.dto.request.AttachedFileRequestDto;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequestDto;
import NestNet.NestNetWebSite.repository.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.ExamCollectionPostRepository;
import NestNet.NestNetWebSite.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    // 족보 게시판에 게시물 저장  --> 테스트 후 리스트로 리팩토링하기
    @Transactional
    public void savePost(ExamCollectionPostRequestDto examCollectionPostRequestDto, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId);

        ExamCollectionPost post = examCollectionPostRequestDto.toEntity(member);

        List<AttachedFile> attachedFileList = new ArrayList<>();
        for(MultipartFile file : files){
            AttachedFile attachedFile = new AttachedFile(post, file);
            attachedFileList.add(attachedFile);
            post.addAttachedFiles(attachedFile);
        }

        examCollectionPostRepository.save(post);
        attachedFileRepository.saveAll(attachedFileList);
    }
}
