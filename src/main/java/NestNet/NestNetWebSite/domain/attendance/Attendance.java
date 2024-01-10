package NestNet.NestNetWebSite.domain.attendance;

import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 출석 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class Attendance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /*
    생성자
     */
    public Attendance(Member member) {
        this.time = LocalDateTime.now();
        this.member = member;
    }
}
