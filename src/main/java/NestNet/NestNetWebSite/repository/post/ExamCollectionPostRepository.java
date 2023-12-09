package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamCollectionPostRepository extends JpaRepository<ExamCollectionPost, Long> {

    // Id(PK)로 단건 조회
    Optional<ExamCollectionPost> findById(Long id);

    // 족보 게시물 조건에 따른 리스트 조회 (페이징)
    @Query("select p from ExamCollectionPost p where" +
            "(:subject is null or p.subject =: subject )" + " and " +
            "(:professor is null or p.professor =: professor )" + " and " +
            "(:year is null or p.year =: year )" + " and " +
            "(:semester is null or p.semester =: semester )" + " and " +
            "(:examType is null or p.examType =: examType )" +
            "order by p.id desc")
    Page<ExamCollectionPost> findAllByFilter(@Param("subject") String subject, @Param("professor") String professor,
                                             @Param("year") Integer year, @Param("semester") Integer semester, @Param("examType") ExamType examType,
                                             Pageable pageable);

    // 족보 게시물 모두 조회
    @Query("select p from ExamCollectionPost p order by p.id desc")
    List<ExamCollectionPost> findAll();

    // 족보 게시물 개수 제한에 따른 리스트 조회
    @Query("select p from ExamCollectionPost p order by p.id desc")
    Page<ExamCollectionPost> findAll(Pageable pageable);

}
