package NestNet.NestNetWebSite.service.attachedfile;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.repository.post.AttachedFileRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachedFileService {

    private final AttachedFileRepository attachedFileRepository;
    private final EntityManager entityManager;

    // Post 자식 객체를 가져오기 위한 간단한 로직 수행
    public Post findPost(Long postId){
        return entityManager.find(Post.class, postId);
    }

    /*
    첨부파일 저장 -> 저장 로직은 게시물에 종속적. 게시물 저장하는 서비스 로직에서 수행
     */

    /*
    게시물에 해당된 첨부파일 모두 조회
     */
    public List<AttachedFileDto> findAllFilesByPost(Long postId){

        List<AttachedFile> files = attachedFileRepository.findByPost(findPost(postId));

        List<AttachedFileDto> fileDtos = new ArrayList<>();
        for(AttachedFile file : files){
            fileDtos.add(new AttachedFileDto(file.getOriginalFileName(), file.getSaveFileName(), file.getSaveFilePath()));
        }

        return fileDtos;
    }
}
