package NestNet.NestNetWebSite.repository.attachedfile;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AttachedFileRepository {

    private final EntityManager entityManager;

    // 저장
    public void saveAll(List<AttachedFile> attachedFiles, List<MultipartFile> files){

        for(AttachedFile attachedFile : attachedFiles){
            entityManager.persist(attachedFile);
        }
        for(int i = 0; i < attachedFiles.size(); i++){
            entityManager.persist(attachedFiles.get(i));
            Path savePath = Paths.get(attachedFiles.get(i).getSaveFilePath()+ File.separator + attachedFiles.get(i).getSaveFileName());
            log.info("AttachedFileRepository / saveAll : 저장 파일 : " + savePath);
            try {
                files.get(i).transferTo(savePath);          //파일 실제 위치에 저장
            } catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    //=========================================조회=========================================//

    // id(PK)로 단건 조회
    public AttachedFile findById(Long id){
        return entityManager.find(AttachedFile.class, id);
    }

    // 게시물에 해당하는 파일 조회
    public List<AttachedFile> findByPost(Post post){
        return entityManager.createQuery("select a from AttachedFile a where a.post =: post")
                .setParameter("post", post)
                .getResultList();
    }

    //====================================================================================//
}