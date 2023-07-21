package NestNet.NestNetWebSite.domain.manager;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class MemberSignUpManagement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_sign_up_request_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MemberAuthority requestMemberAuthority;

    @ColumnDefault("false")
    private boolean isComplete;

    public MemberSignUpManagement(Member member, MemberAuthority requestMemberAuthority) {
        this.member = member;
        this.requestMemberAuthority = requestMemberAuthority;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
