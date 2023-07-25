package NestNet.NestNetWebSite.domain.comment;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;                                            // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Post_id")
    private Post post;                                          // 댓글이 포함된 게시물

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                                      // 댓글 작성한 회원

    @Column(columnDefinition = "TEXT")
    private String content;                                     // 댓글 내용

    /*
    생성자
     */
    public Comment(Post post, Member member, String content){
        this.post = post;
        this.member = member;
        this.content = content;
    }

    //== 비지니스 로직 ==//
    /*
    댓글 수정
     */
    public void modifyContent(String content) {
        this.content = content;
    }

}
