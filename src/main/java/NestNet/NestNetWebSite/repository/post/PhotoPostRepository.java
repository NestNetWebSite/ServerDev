package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.config.redis.RedisUtil;
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
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PhotoPostRepository {

    private final EntityManager entityManager;
    private final RedisUtil redisUtil;

    // 저장
    public void save(Post post){
        entityManager.persist(post);
    }

    // 조회수 update
    public void addViewCount(Post post, String memberLoginId){

        String viewRecordKey = memberLoginId + "_" + post.getId().toString();     //사용자아이디 + 게시물 id

        //24시간 내에 다시 조회해도 조회수 올라가지 않음 (조회하지 않았으면 레디스에 없음 -> 조회수 + 1)
        if(!redisUtil.hasKey(viewRecordKey)){
            post.addViewCount();        //변경 감지에 의해 update
            redisUtil.setData(viewRecordKey, "v", 24, TimeUnit.HOURS);      //24시간 유지 -> 자동 삭제
        }
    }

    // 좋아요
    public void like(Post post){
        post.like();
    }
    // 좋아요 취소
    public void cancelLike(Post post){
        post.cancelLike();
    }

    //=========================================조회=========================================//

    // Id(PK)로 단건 조회
    public PhotoPost findById(Long id){
        return entityManager.find(PhotoPost.class, id);
    }

    // 사진 게시판 리스트 조회(페이징)
    public List<PhotoPost> findAllPhotoPostByPaging(int offset, int limit){

        return entityManager.createQuery("select p from PhotoPost p order by p.id desc", PhotoPost.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    //=====================================================================================//

    // 사진 게시물 삭제
    public void deletePost(Post post){

        entityManager.remove(post);
    }

}
