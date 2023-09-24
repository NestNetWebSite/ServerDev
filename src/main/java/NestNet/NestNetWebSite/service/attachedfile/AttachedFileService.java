package NestNet.NestNetWebSite.service.attachedfile;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    게시물에 해당된 첨부파일 이름 모두 조회
     */
    public List<AttachedFileResponse> findAllFilesByPost(Long postId){

        List<AttachedFile> files = attachedFileRepository.findByPost(findPost(postId));

        List<AttachedFileResponse> fileResponseList = new ArrayList<>();

        for(AttachedFile file : files){
            fileResponseList.add(new AttachedFileResponse(file.getOriginalFileName(), file.getSaveFileName()));
        }

        return fileResponseList;
    }

    /*
    postId와 saveFileName으로 실제 파일 조회
     */
    public InputStreamResource findFile(Long postId, String saveFileName){

        AttachedFile file = attachedFileRepository.findByPostAndFileName(findPost(postId), saveFileName);

        InputStreamResource resource = null;

        try{
            File realFile = new File(file.getSaveFilePath() + File.separator + file.getSaveFileName());
            resource = new InputStreamResource(new FileInputStream(realFile));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return resource;
    }


}
