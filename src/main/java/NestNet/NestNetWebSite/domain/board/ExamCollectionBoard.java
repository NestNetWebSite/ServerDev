package NestNet.NestNetWebSite.domain.board;

import NestNet.NestNetWebSite.domain.BoardCategory;
import NestNet.NestNetWebSite.domain.ExamType;
import NestNet.NestNetWebSite.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class ExamCollectionBoard extends Board{

    private String professsor;

    private String year;                //출제 년도

    private String semester;            //출제 학기

    @Enumerated(EnumType.STRING)
    private ExamType examType;          //중간/기말

    @OneToMany(mappedBy = "examCollectionBoard")
    private List<AttachedFile> attachedFileList = new ArrayList<>();    //첨부파일


}
