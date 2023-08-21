package NestNet.NestNetWebSite.repository.member;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
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
    public Member findByLoginId(String loginId){

        List<Member> member = entityManager.createQuery("select m from Member m where m.loginId =: loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();

        if(member.isEmpty()){
            return null;
        }

        return member.get(0);
    }

    // 이름 + 이메일로 회원 단건 조회
    public Optional<Member> findByNameAndEmail(String name, String emailAddress){

        List<Member> member = entityManager.createQuery("select m from Member m where m.name =: name and m.emailAddress =: emailAddress", Member.class)
                .setParameter("name", name)
                .setParameter("emailAddress", emailAddress)
                .getResultList();

        return member.stream().findAny();
    }

    // 권한으로 회원 조회
    public List<Member> findByAuthority(MemberAuthority memberAuthority){
        return entityManager.createQuery("select m from Member m where m.memberAuthority =: memberAuthority", Member.class)
                .setParameter("memberAuthority", memberAuthority)
                .getResultList();
    }

    // 모든 회원 조회
    public List<Member> findAllMember(String name, MemberAuthority memberAuthority){
        return entityManager.createQuery("select m from Member m where m.name <> '알수없음'" + " and " +
                        "(:name is null or m.name =: name)" + " and " +
                        "(:memberAuthority is null or m.memberAuthority =: memberAuthority)", Member.class)
                .setParameter("name", name)
                .setParameter("memberAuthority", memberAuthority)
                .getResultList();
    }

}
