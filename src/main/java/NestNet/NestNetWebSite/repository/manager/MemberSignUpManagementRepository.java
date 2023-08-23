package NestNet.NestNetWebSite.repository.manager;

import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberSignUpManagementRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(MemberSignUpManagement memberSignUpManagement){
        entityManager.persist(memberSignUpManagement);
    }

    //=========================================조회=========================================//

    // 회원가입 요청 모두 조회
    public List<MemberSignUpManagement> findAll(){

        List<MemberSignUpManagement> resultList = entityManager.createQuery(
                "select mr from  MemberSignUpManagement mr where mr.isComplete = false",
                        MemberSignUpManagement.class)
                .getResultList();

        return resultList;
    }

    // 회원가입 요청 단건 조회
    public MemberSignUpManagement findById(Long id){
        return entityManager.find(MemberSignUpManagement.class, id);
    }

    // 회원가입 요청 Member로 조회
    public Optional<MemberSignUpManagement> findByMember(Member member){

        try {
            MemberSignUpManagement memberSignUpManagement =  entityManager.createQuery(
                            "select mr from MemberSignUpManagement mr where mr.member =: member and mr.isComplete = false ",
                            MemberSignUpManagement.class)
                    .setParameter("member", member)
                    .getSingleResult();

            return Optional.ofNullable(memberSignUpManagement);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    //====================================================================================//
}
