package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import NestNet.NestNetWebSite.domain.post.photo.ThumbNail;
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
public class ThumbNailRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(ThumbNail thumbNail, MultipartFile file){

        entityManager.persist(thumbNail);
        Path savePath = Paths.get(thumbNail.getSaveFilePath()+ File.separator + thumbNail.getSaveFileName());
        log.info("ThumbNailRepository / save : 저장 파일 : " + savePath);
        try {
            file.transferTo(savePath);      //파일 실제 위치에 저장
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //=========================================조회=========================================//

    // Id(PK)로 단건 조회
    public ThumbNail findById(Long id){
        return entityManager.find(ThumbNail.class, id);
    }

    // 사진 게시판 썸네일 모두 조회 (페이징)
    public List<ThumbNail> findAllPhotoThumbNailByPaging(int offset, int limit){

        return entityManager.createQuery("select t from ThumbNail t", ThumbNail.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    //=====================================================================================//

    // 사진 게시물 썸네일 삭제
    public void deleteThumbNail(Long id){

        ThumbNail thumbNail = entityManager.find(ThumbNail.class, id);

        entityManager.remove(thumbNail);
    }
}
