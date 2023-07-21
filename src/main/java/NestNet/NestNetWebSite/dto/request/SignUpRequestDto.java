package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String loginId;                         // 로그인 아이디
    private String loginPassword;                   // 로그인 비밀번호
    private String name;                            // 이름 (본명)
    private boolean graduated;                      // 졸업 여부
    private int graduateYear;                       // 졸업 년도
    private String studentId;                       // 학번
    private int grade;                              // 학년
    private String emailAddress;                    // 이메일 주소
    private MemberAuthority memberAuthority;        // 권한 (회장, 부회장, 임원, 재학생, 졸업생, 관리자)

    public void setMemberAuthority(MemberAuthority memberAuthority) {
        this.memberAuthority = memberAuthority;
    }

    public Member toEntity(){
        return Member.builder()
                .loginId(this.loginId)
                .loginPassword(this.loginPassword)
                .name(this.name)
                .graduated(this.graduated)
                .graduateYear(this.graduateYear)
                .studentId(this.studentId)
                .grade(this.grade)
                .emailAddress(this.emailAddress)
                .memberAuthority(this.memberAuthority)
                .joinDate(LocalDateTime.now())
                .build();
    }
}
