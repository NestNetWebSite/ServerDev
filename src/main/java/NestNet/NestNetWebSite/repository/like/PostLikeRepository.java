package NestNet.NestNetWebSite.repository.like;

import NestNet.NestNetWebSite.domain.like.PostLike;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(PostLike postLike){
        entityManager.persist(postLike);
    }

    //=========================================조회=========================================//

    // 게시물로 조회
    public List<PostLike> findAllByPost(Post post){

        return entityManager.createQuery("select p from PostLike p where p.post =: post", PostLike.class)
                .setParameter("post", post)
                .getResultList();
    }

    // 회원, 게시물로 조회
    public Optional<PostLike> findByMemberAndPost(Member member, Post post){

        try {
            PostLike findPostLike = entityManager.createQuery("select l from PostLike l where l.post =: post and l.member =: member", PostLike.class)
                    .setParameter("post", post)
                    .setParameter("member", member)
                    .getSingleResult();

            return Optional.ofNullable(findPostLike);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // 회원 좋아요 상태 조회
    public boolean isMemberLiked(Post post, Member member){

        try {
            PostLike postLike = entityManager.createQuery("select l from PostLike l where l.post =: post and l.member =: member", PostLike.class)
                    .setParameter("post", post)
                    .setParameter("member", member)
                    .getSingleResult();

        } catch (NoResultException e){
            return false;
        }
        return true;
    }

    //====================================================================================//

    // 삭제
    public void delete(PostLike postLike){
        entityManager.remove(postLike);
    }

    // 여러개 삭제
    public void deleteAll(List<PostLike> postLikeList){

        for(PostLike postLike : postLikeList){
            entityManager.remove(postLike);
        }
    }
}
