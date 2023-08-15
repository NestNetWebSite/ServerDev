package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.config.redis.RedisUtil;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ExamCollectionPostRepository {

    private final EntityManager entityManager;
    private final RedisUtil redisUtil;

    // 저장
    public void save(Post post){
        entityManager.persist(post);
    }

    // 조회수 update
    public void addViewCount(Post post, String memberLoginId){

        String viewRecordKey = memberLoginId + "_" +  post.getId().toString();     //사용자아이디 + 게시물 id

        //24시간 내에 조회하지 않았으면 레디스에 없음 -> 조회수 + 1
        if(!redisUtil.hasKey(viewRecordKey)){
            post.addViewCount();        //변경 감지에 의해 update
//            redisUtil.setData(viewRecordKey, "v", 24, TimeUnit.HOURS);      //24시간 유지 -> 자동 삭제
            redisUtil.setData(viewRecordKey, "v", 1000*8);      //8초 유지 -> 자동 삭제
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
    public ExamCollectionPost findById(Long id){
        return entityManager.find(ExamCollectionPost.class, id);
    }

    // 족보 게시물 조건에 따른 리스트 조회
    public List<ExamCollectionPost> findAllExamCollectionPostByFilter(String subject, String professor, Integer year, Integer semester, ExamType examType){

        List<ExamCollectionPost> resultList = entityManager.createQuery(
                "select p from ExamCollectionPost p where" +
                        "(:subject is null or p.subject =: subject )" + " and " +
                        "(:professor is null or p.professor =: professor )" + " and " +
                        "(:year is null or p.year =: year )" + " and " +
                        "(:semester is null or p.semester =: semester )" + " and " +
                        "(:examType is null or p.examType =: examType )", ExamCollectionPost.class)
                .setParameter("subject", subject)
                .setParameter("professor", professor)
                .setParameter("year", year)
                .setParameter("semester", semester)
                .setParameter("examType", examType)
                .getResultList();

        return resultList;
    }

    // 족보 게시물 모두 조회
    public List<ExamCollectionPost> findAllExamCollectionPost(){

        List<ExamCollectionPost> resultList = entityManager.createQuery("select p from ExamCollectionPost p", ExamCollectionPost.class)
                .getResultList();

        return resultList;
    }

    // 족보 게시물 개수 제한에 따른 리스트 조회
    public List<ExamCollectionPost> findExamCollectionPostLimit(int offset, int limit){

        List<ExamCollectionPost> resultList = entityManager.createQuery("select p from ExamCollectionPost p", ExamCollectionPost.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    //=====================================================================================//

    // 족보 게시물 삭제
    public void deletePost(Long postId){

        Post post = entityManager.find(Post.class, postId);
        entityManager.remove(post);
    }
}
