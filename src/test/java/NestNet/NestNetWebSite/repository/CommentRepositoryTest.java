package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.comment.Comment;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.repository.comment.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    CommentRepository commentRepository;

    @Test
    public void 댓글_작성_조회(){     //한 사용자가 한 게시물에 두개 이상 댓글 다는 행위에 대한 테스트
        //given
        Post post = new ExamCollectionPost();
        Member member = new Member();
        Comment comment1 = new Comment(post, member, "테스트");
        Comment comment2 = new Comment(post, member, "테스트2");

        //when
        entityManager.persist(post);
        entityManager.persist(member);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        List<Comment> commentList = commentRepository.findCommentsByPost(post);

        //then
        Assertions.assertEquals(comment1.getContent(), commentList.get(0).getContent());
        Assertions.assertEquals(comment2.getContent(), commentList.get(1).getContent());
    }
}
