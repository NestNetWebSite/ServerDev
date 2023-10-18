package NestNet.NestNetWebSite.service.attachedfile;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.PostRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachedFileService {

    private final AttachedFileRepository attachedFileRepository;
    private final PostRepository postRepository;
    private final EntityManager entityManager;

    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

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
            fileResponseList.add(new AttachedFileResponse(file.getId(), file.getOriginalFileName(), file.getSaveFilePath(), file.getSaveFileName()));
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
            File realFile = new File(basePath + file.getSaveFilePath() + File.separator + file.getSaveFileName());
            resource = new InputStreamResource(new FileInputStream(realFile));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return resource;
    }

    /*
    첨부파일 수정
     */
    @Transactional
    public boolean modifyFiles(List<Long> existFileIdList, List<MultipartFile> fileList, Long postId){

        Post post = postRepository.findById(postId);

        List<AttachedFile> deleteFileList = attachedFileRepository.findByPost(post);      //이전에 있던 파일 중 삭제할 파일

        // 이전에 있던 파일 중, 삭제될 것만 남김
        for(int i = 0; i < deleteFileList.size(); i++){
            for(Long existFileId : existFileIdList){
                if(deleteFileList.get(i).getId() == existFileId){
                    deleteFileList.remove(deleteFileList.get(i));
                }
            }
        }

        boolean isDeleted = attachedFileRepository.deleteFiles(deleteFileList);       // 기존 파일 중 삭제된 파일 삭제

        List<AttachedFile> newAttachedFileList = new ArrayList<>();     // 새로 입력된 파일

        for(MultipartFile file : fileList){
            newAttachedFileList.add(new AttachedFile(post, file));
        }

        boolean isSaved = attachedFileRepository.saveAll(newAttachedFileList, fileList);

        if(isSaved && isDeleted) return true;
        else return false;
    }

    /*
    첨부파일 삭제
     */
    @Transactional
    public boolean deleteFiles(Long postId){

        Post post = postRepository.findById(postId);
        List<AttachedFile> files = attachedFileRepository.findByPost(post);

        return attachedFileRepository.deleteFiles(files);
    }
}
