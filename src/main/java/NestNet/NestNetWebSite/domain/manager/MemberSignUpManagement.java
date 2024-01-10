package NestNet.NestNetWebSite.domain.manager;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * 사용자의 회원가입 요청 정보를 저장하는 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class MemberSignUpManagement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_sign_up_request_id")
    private Long id;                                            // PK

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                                      // 회원 (신청한)

    @Enumerated(EnumType.STRING)
    private MemberAuthority requestMemberAuthority;             // 회원이 신청한 권한

    @ColumnDefault("false")
    private boolean isComplete;                                 // 회원가입 승인 여부

    /*
    생성자
     */
    public MemberSignUpManagement(Member member, MemberAuthority requestMemberAuthority) {
        this.member = member;
        this.requestMemberAuthority = requestMemberAuthority;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
