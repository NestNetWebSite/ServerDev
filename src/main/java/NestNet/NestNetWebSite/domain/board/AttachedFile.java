package NestNet.NestNetWebSite.domain.board;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter @Builder
public class AttachedFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attached_file_id")
    private Long id;                                            // PK

    @ManyToOne(fetch = FetchType.LAZY)  //단방향
    @JoinColumn(name = "board_id")
    private Board board;                                        // 게시판

    private String originalFileName;                                    // 파일 이름
    private String saveFileName;
    private String saveFilePath;                                    // 파일 경로 (서버)

}
