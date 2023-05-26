package NestNet.NestNetWebSite.domain.board;

import NestNet.NestNetWebSite.domain.board.ExamCollectionBoard;
import jakarta.persistence.*;

@Entity
public class AttachedFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attached_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_collection_board_id")
    private ExamCollectionBoard examCollectionBoard;



}
