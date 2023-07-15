package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberSignUpManagementRepository {

    private final EntityManager entityManager;

    public void save(MemberSignUpManagement memberSignUpManagement){
        entityManager.persist(memberSignUpManagement);
    }

    // 회원가입 요청 모두 조회
    public List<MemberSignUpManagement> findAll(){
        List<MemberSignUpManagement> resultList = entityManager.createQuery("select mr from  MemberSignUpManagement mr")
                .getResultList();

        return resultList;
    }

    // 회원가입 요청 단건 조회
    public MemberSignUpManagement findById(Long id){
        return entityManager.find(MemberSignUpManagement.class, id);
    }

    // 회원가입 요청 Member로 조회
    public MemberSignUpManagement findByMember(Member member){

        return entityManager.createQuery("select mr from MemberSignUpManagement mr where mr.member =: member", MemberSignUpManagement.class)
                .setParameter("member", member)
                .getResultList().get(0);
    }
}
