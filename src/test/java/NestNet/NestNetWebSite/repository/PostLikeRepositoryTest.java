package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.like.PostLike;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.repository.comment.CommentRepository;
import NestNet.NestNetWebSite.repository.like.PostLikeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PostLikeRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    PostLikeRepository postLikeRepository;

    @Test
    public void 좋아요_수_조회(){
        //given
        Post post = new ExamCollectionPost();
        Member member = new Member();
        Member member2 = new Member();
        PostLike postLike = new PostLike(post, member);
        PostLike postLike2 = new PostLike(post, member2);

        //when
        entityManager.persist(post);
        entityManager.persist(member);
        entityManager.persist(member2);
        postLikeRepository.save(postLike);
        postLikeRepository.save(postLike2);
        postLikeRepository.delete(postLike2);

        //then
//        Assertions.assertEquals(1L, postLikeRepository.likeCount(post));
    }

    @Test
    public void 회원_좋아요_상태_조회(){
        //given
        Post post = new ExamCollectionPost();
        Member member = new Member();
        Member member2 = new Member();
        PostLike postLike = new PostLike(post, member);

        //when
        entityManager.persist(post);
        entityManager.persist(member);
        entityManager.persist(member2);
        postLikeRepository.save(postLike);

        //then
        Assertions.assertEquals(true, postLikeRepository.isMemberLiked(post, member));
        Assertions.assertEquals(false, postLikeRepository.isMemberLiked(post, member2));
    }
}
