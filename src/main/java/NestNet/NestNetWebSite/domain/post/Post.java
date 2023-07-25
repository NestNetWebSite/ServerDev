package NestNet.NestNetWebSite.domain.post;

import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시물 추상클래스 (모든 게시물의 부모)
 */
@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor
public abstract class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Post_id")
    private Long id;                                                // PK

    private String title;                                           // 제목

    @Column(columnDefinition = "TEXT")
    private String bodyContent;                                     // 본문

    @ManyToOne(fetch = FetchType.LAZY)  //단반향
    @JoinColumn(name = "member_id")
    private Member member;                                          // 작성 멤버

    private int viewCount;                                          // 조회 수

    private int recommendationCount;                                // 추천 수

    @Enumerated(value = EnumType.STRING)
    private PostCategory PostCategory;                              // 게시판 분류

    private LocalDateTime createdTime;                              // 글 쓴 시각

    private LocalDateTime modifiedTime;                             // 글 수정한 시각

    /*
    생성자
     */
    @Builder
    public Post(String title, String bodyContent, Member member, int viewCount, int recommendationCount, PostCategory postCategory, LocalDateTime createdTime) {
        this.title = title;
        this.bodyContent = bodyContent;
        this.member = member;
        this.viewCount = viewCount;
        this.recommendationCount = recommendationCount;
        PostCategory = postCategory;
        this.createdTime = createdTime;
    }

    //== 비지니스 로직 ==//
    /*
    게시글 수정
     */
    public abstract void modifyPost(String title, String bodyContent);

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
