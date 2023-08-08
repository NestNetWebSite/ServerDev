package NestNet.NestNetWebSite.repository.like;

import NestNet.NestNetWebSite.domain.like.PostLike;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(PostLike postLike){

        try {
            PostLike findPostLike = entityManager.createQuery("select l from PostLike l where l.post =: post and l.member =: member", PostLike.class)
                    .setParameter("post", postLike.getPost())
                    .setParameter("member", postLike.getMember())
                    .getSingleResult();

        } catch (NoResultException e){
            entityManager.persist(postLike);
        }
    }

    //=========================================조회=========================================//

    // 좋아요 수 조회     -> 좋아요 수는 엔티티에 저장
//    public Long likeCount(Post post){
//
//        Long count = entityManager.createQuery("select COUNT(l) from PostLike l where l.post =: post", Long.class)
//                .setParameter("post", post)
//                .getSingleResult();
//
//        return count;
//    }

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
    public void delete(Post post, Member member){

        try {
            PostLike findPostLike = entityManager.createQuery("select l from PostLike l where l.post =: post and l.member =: member", PostLike.class)
                    .setParameter("post", post)
                    .setParameter("member", member)
                    .getSingleResult();
            entityManager.remove(findPostLike);

        } catch (NoResultException e){
           //여기 처리 어떻게?
        }

    }
}
