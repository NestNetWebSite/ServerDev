package NestNet.NestNetWebSite.domain.board;

import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Dtype")
@Getter
public abstract class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;                                            // PK

    private String title;                                       // 제목

    private String BodyContent;                                 // 본문

    @ManyToOne(fetch = FetchType.LAZY)  //단반향
    @JoinColumn(name = "member_id")
    private Member member;                                      // 글 쓴 멤버

//    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
//    private List<Comment> comments = new ArrayList<>();          // 댓글

    private int viewCount;                                       // 조회 수

    private int recommendationCount;                             // 추천 수

    @Enumerated(value = EnumType.STRING)
    private BoardCategory boardCategory;                         // 게시판 분류

    private LocalDateTime createdTime;                           // 글 쓴 시각

    private LocalDateTime modifiedTime;                          // 글 수정한 시각

    //== setter ==//

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBodyContent(String bodyContent) {
        BodyContent = bodyContent;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
