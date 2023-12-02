package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.config.redis.RedisUtil;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPost;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UnifiedPostRepository {

    private final EntityManager entityManager;
    private final RedisUtil redisUtil;

    // 저장
    public void save(UnifiedPost post){
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

//    // 좋아요
//    public void like(Post post){
//        post.like();
//    }
//    // 좋아요 취소
//    public void cancelLike(Post post){
//        post.cancelLike();
//    }

    //=========================================조회=========================================//

    // Id(PK)로 단건 조회
    public UnifiedPost findById(Long id){
        return entityManager.find(UnifiedPost.class, id);
    }

    // 통합 게시판 게시물 총 갯수 조회
    public Long findTotalSize(UnifiedPostType unifiedPostType){

        Object size = entityManager.createQuery("select count(u) from UnifiedPost u where " +
                        "(:unifiedPostType is null or u.unifiedPostType =: unifiedPostType)")
                .setParameter("unifiedPostType", unifiedPostType)
                .getResultList().get(0);

        return Long.valueOf(String.valueOf(size));
    }

    // 통합 게시판 (자유 / 개발 / 진로) 조회
    public List<UnifiedPost> findUnifiedPostByType(int offset, int limit, UnifiedPostType unifiedPostType){

        List<UnifiedPost> resultList = entityManager.createQuery(
                "select u from UnifiedPost u where " +
                        "(:unifiedPostType is null or u.unifiedPostType =: unifiedPostType) " +
                        "order by u.id desc")
                .setParameter("unifiedPostType", unifiedPostType)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    //====================================================================================//

    // 통합 게시물 삭제
    public void deletePost(Post post){

        entityManager.remove(post);
    }
}
