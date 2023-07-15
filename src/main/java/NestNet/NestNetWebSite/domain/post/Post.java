package NestNet.NestNetWebSite.domain.post;

import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Getter
public abstract class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Post_id")
    private Long id;                                            // PK

    private String title;                                       // 제목

    private String bodyContent;                                 // 본문

    @ManyToOne(fetch = FetchType.LAZY)  //단반향
    @JoinColumn(name = "member_id")
    private Member member;                                      // 글 쓴 멤버

//    @OneToMany(mappedBy = "Post", cascade = CascadeType.ALL)
//    private List<Comment> comments = new ArrayList<>();          // 댓글

    private int viewCount;                                       // 조회 수

    private int recommendationCount;                             // 추천 수

    @Enumerated(value = EnumType.STRING)
    private PostCategory PostCategory;                         // 게시판 분류

    private LocalDateTime createdTime;                           // 글 쓴 시각

    private LocalDateTime modifiedTime;                          // 글 수정한 시각

    public Post() {}

    public Post(String title, String bodyContent, Member member, int viewCount, int recommendationCount, PostCategory PostCategory, LocalDateTime createdTime) {
        this.title = title;
        this.bodyContent = bodyContent;
        this.member = member;
        this.viewCount = viewCount;
        this.recommendationCount = recommendationCount;
        this.PostCategory = PostCategory;
        this.createdTime = createdTime;
    }

    //== setter ==//

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
