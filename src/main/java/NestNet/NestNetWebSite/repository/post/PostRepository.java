package NestNet.NestNetWebSite.repository.post;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepository {

    private final EntityManager entityManager;

    // PK(id)로 조회
    public Post findById(Long postId){
        return entityManager.find(Post.class, postId);
    }

    // 사용자가 쓴 글 조회 (족보, 통합 게시판 / 사진 게시판은 제외)
    public List<Post> findAllPostByMember(Member member){

        List<Post> postList = entityManager.createQuery("select p from Post p where p.member =: member and p.PostCategory <> 'PHOTO'", Post.class)
                .setParameter("member", member)
                .getResultList();

        return postList;
    }
}
