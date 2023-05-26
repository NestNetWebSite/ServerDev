package NestNet.NestNetWebSite.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId;

    private String loginPassword;

    private String name;

    private String studentId;

    private String emailAddress;

    @Enumerated(EnumType.STRING)
    private MemberLevel level;

    private LocalDateTime joinDate;
}
