package NestNet.NestNetWebSite.repository.attachedfile;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AttachedFileRepository {

    private final EntityManager entityManager;
    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    // 저장
    public boolean saveAll(List<AttachedFile> attachedFiles, List<MultipartFile> files){

        for(int i = 0; i < attachedFiles.size(); i++){
            Path savePath = Paths.get(basePath + attachedFiles.get(i).getSaveFilePath()+ File.separator + attachedFiles.get(i).getSaveFileName());
            log.info("AttachedFileRepository / saveAll : 저장 파일 : " + savePath);

            try {
                files.get(i).transferTo(savePath);          //파일 실제 위치에 저장
                entityManager.persist(attachedFiles.get(i));
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    // 수정
//    public boolean modifyAll(List<AttachedFile> attachedFiles, List<MultipartFile> files){
//
//        for(int i = 0; i < attachedFiles.size(); i++){
//
//            Path savePath = Paths.get(folder.getAbsolutePath() + File.separator + attachedFiles.get(i).getSaveFileName());
//
//            log.info("AttachedFileRepository / saveAll : 저장 파일 : " + savePath);
//
//            try {
//                files.get(i).transferTo(savePath);          //파일 실제 위치에 저장
//                entityManager.persist(attachedFiles.get(i));
//            } catch (Exception e){
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }


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

    // 게시물 + 파일명으로 파일 단건 조회
    public AttachedFile findByPostAndFileName(Post post, String saveFileName){

        List<AttachedFile> attachedFileList =  entityManager.createQuery("select a from AttachedFile a where a.post =: post and a.saveFileName =: saveFileName")
                .setParameter("post", post)
                .setParameter("saveFileName", saveFileName)
                .getResultList();

        if(!attachedFileList.isEmpty()){
            return attachedFileList.get(0);
        }
        return null;
    }

    //====================================================================================//

    // 파일 삭제
    public boolean deleteFiles(List<AttachedFile> attachedFiles){

        for(int i = 0; i <attachedFiles.size(); i++){
            Path delPath = Paths.get(basePath + attachedFiles.get(i).getSaveFilePath()+ File.separator + attachedFiles.get(i).getSaveFileName());
            log.info("AttachedFileRepository / deleteFiles : 삭제 파일 : " + delPath);

            entityManager.remove(attachedFiles.get(i));

            try {
                File delFile = new File(delPath.toString());

                if(delFile.exists()){
                    delFile.delete();
                }
            } catch (Exception e){
                log.info(e.getMessage());
                return false;
            }
        }
        return true;
    }
}
