package NestNet.NestNetWebSite.domain.board;

import NestNet.NestNetWebSite.domain.BoardCategory;
import NestNet.NestNetWebSite.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Dtype")
@Getter @Setter
public abstract class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;                   //제목

    private String BodyContent;             //본문

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                  //글 쓴 멤버

    private int viewCount;                  //조회 수

    private int recommendationCount;        //추천 수

    private BoardCategory boardCategory;    //게시판 분류

    private LocalDateTime createdTime;      //글 쓴 시각

    private LocalDateTime modifiedTime;     //글 수정한 시각
}
