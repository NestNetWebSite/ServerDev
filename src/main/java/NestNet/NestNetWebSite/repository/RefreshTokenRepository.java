package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.token.RefreshToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Queue;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(RefreshToken refreshToken){
        entityManager.persist(refreshToken);
    }

    // Id(PK)로 단건 조회
    public RefreshToken findById(Long id){
        return entityManager.find(RefreshToken.class, id);
    }

    // access 토큰으로 단건 조회
    public RefreshToken findByAccessToken(String accessToken){

        RefreshToken findRefreshToken = entityManager.createQuery(
                "select rt from RefreshToken rt where rt.accessToken =: accessToken", RefreshToken.class)
                .setParameter("accessToken", accessToken)
                .getSingleResult();

        return findRefreshToken;
    }

    // refresh 토큰으로 단건 조회
    public RefreshToken findByRefreshToken(String refreshToken){

        RefreshToken findRefreshToken = entityManager.createQuery(
                        "select rt from RefreshToken rt where rt.refreshToken =: refreshToken", RefreshToken.class)
                .setParameter("refreshToken", refreshToken)
                .getSingleResult();

        return findRefreshToken;
    }

    // 삭제
    public int delete(Long id){

        Query query = entityManager.createQuery("delete from RefreshToken rt where rt.id =: id")
                .setParameter("id", id);

        int rows = query.executeUpdate();

        return rows;
    }
}
