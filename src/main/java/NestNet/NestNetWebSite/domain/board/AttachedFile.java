package NestNet.NestNetWebSite.domain.board;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AttachedFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attached_file_id")
    private Long id;                                            // PK

    @ManyToOne(fetch = FetchType.LAZY)  //단방향
    @JoinColumn(name = "board_id")
    private Board board;                                        // 게시판

    private String fileName;                                    // 파일 이름

    private String filePath;                                    // 파일 경로 (서버)

}
