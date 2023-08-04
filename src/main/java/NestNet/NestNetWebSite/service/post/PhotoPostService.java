package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequestDto;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.PhotoPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoPostService {

    private final PhotoPostRepository photoPostRepository;
    private final MemberRepository memberRepository;
    private final AttachedFileRepository attachedFileRepository;

    /*
    사진 게시판에 게시물 저장
     */
    @Transactional
    public void savePost(PhotoPostRequestDto photoPostRequestDto, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId);

        PhotoPost post = photoPostRequestDto.toEntity(member);

        List<AttachedFile> attachedFileList = new ArrayList<>();
        for(MultipartFile file : files){
            AttachedFile attachedFile = new AttachedFile(post, file);
            attachedFileList.add(attachedFile);
            post.addAttachedFiles(attachedFile);
        }

        photoPostRepository.save(post);
        attachedFileRepository.saveAll(attachedFileList, files);
    }
}
