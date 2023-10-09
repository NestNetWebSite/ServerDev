package NestNet.NestNetWebSite.repository.comment;

import NestNet.NestNetWebSite.domain.comment.Comment;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(Comment comment){
        entityManager.persist(comment);
    }

    // 수정
    public void modify(Comment comment, String content){
        comment.modifyContent(content);
    }

    //=========================================조회=========================================//
    // id(PK)로 단건 조회
    public Comment findById(Long id){

        return entityManager.find(Comment.class, id);
    }

    // 게시물에 해당하는 댓글 모두 조회
    public List<Comment> findCommentsByPost(Post post){

        List<Comment> comments = entityManager.createQuery("select c from Comment c where c.post =: post")
                .setParameter("post", post)
                .getResultList();

        return comments;
    }

    //====================================================================================//

    // 삭제
    public void delete(Comment comment){
        entityManager.remove(comment);
    }

    // 댓글 여러개 삭제
    public void deleteAll(List<Comment> commentList){

        for(Comment comment : commentList){
            entityManager.remove(comment);
        }
    }
}
