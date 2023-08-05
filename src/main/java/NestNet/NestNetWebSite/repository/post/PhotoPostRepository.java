package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import NestNet.NestNetWebSite.domain.post.photo.ThumbNail;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PhotoPostRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(Post post){

        entityManager.persist(post);
    }

    // 조회수 update
    public void addViewCount(Post post){
        post.addViewCount();        //변경 감지에 의해 update
    }

    //=========================================조회=========================================//

    // Id(PK)로 단건 조회
    public PhotoPost findById(Long id){
        return entityManager.find(PhotoPost.class, id);
    }

    // 사진 게시판 리스트 조회(페이징)
    public List<PhotoPost> findAllPhotoPostByPaging(int offset, int limit){

        return entityManager.createQuery("select p from PhotoPost p", PhotoPost.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    //=====================================================================================//

    // 사진 게시물 삭제
    public void deletePost(Long postId){

        Post post = entityManager.find(Post.class, postId);

        entityManager.remove(post);
    }

}
