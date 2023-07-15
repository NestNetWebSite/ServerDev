package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.post.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttachedFileRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(AttachedFile attachedFile){
        entityManager.persist(attachedFile);
    }

    // id(PK)로 단건 조회
    public AttachedFile findById(Long id){
        return entityManager.find(AttachedFile.class, id);
    }

    // 게시물에 해당하는 파일 조회
    public List<AttachedFile> findByPost(Post post){
        return entityManager.createQuery("select a from AttachedFile a where a.Post =: post")
                .setParameter("Post", post)
                .getResultList();
    }
}
