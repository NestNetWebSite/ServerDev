package NestNet.NestNetWebSite.repository.comment;

import NestNet.NestNetWebSite.domain.comment.Comment;
import NestNet.NestNetWebSite.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // id(PK)로 단건 조회
    Optional<Comment> findById(Long id);

    // 게시물에 해당하는 댓글 모두 조회
    List<Comment> findAllByPost(Post post);

    // 삭제
    @Modifying
    void delete(Comment comment);

    // 여러개 삭제
    @Modifying
    void deleteAllByPost(Post post);

}
