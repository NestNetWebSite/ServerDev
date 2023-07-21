package NestNet.NestNetWebSite.domain.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;                                // PK

    private String loginId;                         // 로그인 아이디

    private String loginPassword;                   // 로그인 비밀번호

    private String name;                            // 이름 (본명)

    private boolean graduated;                      // 졸업 여부

    private int graduateYear;                       // 졸업 년도

    private String studentId;                       // 학번

    private int grade;                              // 학년

    private String emailAddress;                    // 이메일 주소

    @Enumerated(EnumType.STRING)
    private MemberAuthority memberAuthority;        // 권한 (회장, 부회장, 임원, 재학생, 졸업생, 관리자)

    private LocalDateTime joinDate;                 // 회원가입 날짜

    public Member(String loginId, String loginPassword, String name, String studentId, String emailAddress, MemberAuthority memberAuthority) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.name = name;
        this.studentId = studentId;
        this.emailAddress = emailAddress;
        this.memberAuthority = memberAuthority;
        this.joinDate = LocalDateTime.now();
    }

    //== setter ==//
    public void setMemberAuthority(MemberAuthority memberAuthority) {
        this.memberAuthority = memberAuthority;
    }

    //== 비지니스 로직 ==//
    /*
    비밀번호 암호화
     */
    public Member encodePassword(PasswordEncoder passwordEncoder){
        this.loginPassword = passwordEncoder.encode(this.loginPassword);
        return this;
    }

    /*
    비밀번호가 맞는지 확인
     */
    public boolean checkPassword(String rawPassword, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(rawPassword, this.getLoginPassword());
    }

    /*
    재학생 -> 졸업생으로 전환
     */
    public void changeMemberToGraduate(){
        this.graduated = true;
        this.graduateYear = LocalDateTime.now().getYear();
        this.memberAuthority = MemberAuthority.GRADUATED_MEMBER;
    }

}
