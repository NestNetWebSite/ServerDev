package NestNet.NestNetWebSite.repository.manager;

import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberSignUpManagementRepository extends JpaRepository<MemberSignUpManagement, Long> {

    // 회원가입 요청 단건 조회
    Optional<MemberSignUpManagement> findById(Long id);

    // 회원가입 요청 모두 조회
    List<MemberSignUpManagement> findAll();

    // 미승인된 회원가입 요청 Member로 조회
    @Query("select msm from MemberSignUpManagement msm where msm.member =: member and msm.isComplete = false")
    Optional<MemberSignUpManagement> findByMember(@Param("member") Member member);

}
