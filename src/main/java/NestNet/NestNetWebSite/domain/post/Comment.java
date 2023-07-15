package NestNet.NestNetWebSite.domain.post;

import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;                        // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Post_id")
    private Post Post;                    // 게시판

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                  // 작성 회원

    private String content;                 // 댓글 내용

    //== setter ==//
    public void setContent(String content) {
        this.content = content;
    }
}
