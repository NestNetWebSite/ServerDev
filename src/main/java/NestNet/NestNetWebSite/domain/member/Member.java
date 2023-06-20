package NestNet.NestNetWebSite.domain.member;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;                                // PK

    private String loginId;                         // 로그인 아이디

    private String loginPassword;                   // 로그인 비밀번호

    private String name;                            // 이름 (본명)

    private String studentId;                       // 학번

    private String emailAddress;                    // 이메일 주소

    @Enumerated(EnumType.STRING)
    private MemberAuthority memberAuthority;        // 권한 (회장, 부회장, 임원, 재학생, 졸업생, 관리자)

    private LocalDateTime joinDate;                 // 회원가입 날짜

    //== setter ==//
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setMemberAuthority(MemberAuthority memberAuthority) {
        this.memberAuthority = memberAuthority;
    }
}
