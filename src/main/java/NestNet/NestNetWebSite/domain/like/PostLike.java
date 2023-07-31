package NestNet.NestNetWebSite.domain.like;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 좋아요 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;                                                // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;                                              // 좋아요가 눌린 게시물

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                                          // 좋아요 누른 회원

    /*
    생성자
     */
    public PostLike(Post post, Member member) {
        this.post = post;
        this.member = member;
    }
}
