package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.post.Postable;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPost;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UnifiedPostRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(UnifiedPost post){
        entityManager.persist(post);
    }

    // Id(PK)로 단건 조회
    public UnifiedPost findById(Long id){
        return entityManager.find(UnifiedPost.class, id);
    }

    // 통합 게시판(자유 + 개발 + 진로) 모두 조회
    public List<UnifiedPost> findAllUnifiedPost(int offset, int limit){

        List<UnifiedPost> resultList = entityManager.createNativeQuery(
                        "select * from Post inner join member" +
                                " on Post.member_id = member.member_id" +
                                " inner join unified_post" +
                                " on on Post.Post_id = unified_Post.Post_id" +
                                " where dtype = 'Free' and 'Dev' and 'Career';", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    // 통합 게시판 (자유) 조회
    public List<UnifiedPost> findUnifiedFreePost(int offset, int limit){

        List<UnifiedPost> resultList = entityManager.createNativeQuery(
                        "select * from Post inner join member" +
                                " on Post.member_id = member.member_id" +
                                " inner join unified_post" +
                                " on on Post.post_id = unified_post.post_id" +
                        " where dtype = 'Free';", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    // 통합 게시판 (개발) 조회
    public List<UnifiedPost> findUnifiedDevPost(int offset, int limit){

        List<UnifiedPost> resultList = entityManager.createNativeQuery(
                        "select * from Post inner join member" +
                                " on Post.member_id = member.member_id" +
                                " inner join unified_post" +
                                " on Post.post_id = unified_post.post_id" +
                        " where dtype = 'Dev';", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    // 통합 게시판 (진로) 조회
    public List<UnifiedPost> findUnifiedCareerPost(int offset, int limit){

        List<UnifiedPost> resultList = entityManager.createNativeQuery(
                        "select * from Post inner join member" +
                                " on Post.member_id = member.member_id" +
                                " inner join unified_Post" +
                                " on on Post.post_id = unified_post.post_id" +
                        " where dtype = 'Career';", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }
}