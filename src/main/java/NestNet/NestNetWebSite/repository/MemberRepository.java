package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(Member member){
        entityManager.persist(member);
    }

    // Id(PK)로 회원 단건 조회
    public Member findById(Long id){
        return entityManager.find(Member.class, id);
    }

    // 로그인 아이디로 회원 단건 조회
    public List<Member> findByLoginId(String loginId){
        List<Member> members = entityManager.createQuery("select m from Member m where m.loginId =: loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();

        System.out.println(members);

        return members;

    }

    // 권한으로 회원 조회
    public List<Member> findByAuthority(MemberAuthority memberAuthority){
        return entityManager.createQuery("select m from Member m where m.memberAuthority =: memberAuthority", Member.class)
                .setParameter("memberAuthority", memberAuthority)
                .getResultList();
    }

}
