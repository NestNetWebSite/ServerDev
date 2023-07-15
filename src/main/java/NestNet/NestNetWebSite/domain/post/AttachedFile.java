package NestNet.NestNetWebSite.domain.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.text.Normalizer;
import java.util.UUID;

@Entity
@Getter @Builder
public class AttachedFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attached_file_id")
    private Long id;                                            // PK

    @ManyToOne(fetch = FetchType.LAZY)  //단방향
    @JoinColumn(name = "Post_id")
    private Post post;                                        // 게시판

    private String originalFileName;                                    // 파일 이름
    private String saveFileName;
    private String saveFilePath;                                    // 파일 경로 (서버)


}
