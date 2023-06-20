package NestNet.NestNetWebSite.domain.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("E")
@Getter
public class ExamCollectionBoard extends Board{

    private String subject;                                             // 과목

    private String professsor;                                          // 교수

    private String year;                                                // 출제 년도

    private String semester;                                            // 출제 학기

    @Enumerated(EnumType.STRING)
    private ExamType examType;                                          // 중간/기말 분류

//    @OneToMany(mappedBy = "examCollectionBoard", cascade = CascadeType.ALL)
//    private List<AttachedFile> attachedFileList = new ArrayList<>();    // 첨부파일

    //== setter ==//
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setProfesssor(String professsor) {
        this.professsor = professsor;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }
}
