package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExamCollectionPostRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(Post post){
        entityManager.persist(post);
    }

    //=========================================조회=========================================//

    // Id(PK)로 단건 조회
    public ExamCollectionPost findById(Long id){
        return entityManager.find(ExamCollectionPost.class, id);
    }

    // 족보 게시물 조건에 따른 조회
    public List<ExamCollectionPost> findAllExamCollectionPostByFilter(String subject, String professsor, Integer year, Integer semester, ExamType examType){

        List<ExamCollectionPost> resultList = entityManager.createQuery(
                "select p from ExamCollectionPost p where" +
                        "(:subject is null or p.subject =: subject )" + " and " +
                        "(:professsor is null or p.professsor =: professsor )" + " and " +
                        "(:year is null or p.year =: year )" + " and " +
                        "(:semester is null or p.semester =: semester )" + " and " +
                        "(:examType is null or p.examType =: examType )", ExamCollectionPost.class)
                .setParameter("subject", subject)
                .setParameter("professsor", professsor)
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

    // 족보 게시물 개수 제한에 따른 조회
    public List<ExamCollectionPost> findExamCollectionPostLimit(int offset, int limit){

        List<ExamCollectionPost> resultList = entityManager.createQuery("select p from ExamCollectionPost p", ExamCollectionPost.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }
    //=====================================================================================//

    // 족보 게시물 삭제
    public void deletePost(ExamCollectionPost examCollectionPost){
        entityManager.remove(examCollectionPost);
    }
}
