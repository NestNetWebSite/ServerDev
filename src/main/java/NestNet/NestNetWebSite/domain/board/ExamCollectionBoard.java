package NestNet.NestNetWebSite.domain.board;

import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Exam")
@Getter
public class ExamCollectionBoard extends Board{

    private String subject;                                             // 과목

    private String professsor;                                          // 교수

    private int year;                                                // 출제 년도

    private int semester;                                            // 출제 학기

    @Enumerated(EnumType.STRING)
    private ExamType examType;                                          // 중간/기말 분류

//    @OneToMany(mappedBy = "examCollectionBoard", cascade = CascadeType.ALL)
//    private List<AttachedFile> attachedFileList = new ArrayList<>();    // 첨부파일

    protected ExamCollectionBoard(){}

    @Builder
    public ExamCollectionBoard(String title, String bodyContent, Member member, int viewCount, int recommendationCount, BoardCategory boardCategory,
                               LocalDateTime createdTime, String subject, String professsor, int year, int semester, ExamType examType) {

        super(title, bodyContent, member, viewCount, recommendationCount, boardCategory, createdTime);
        this.subject = subject;
        this.professsor = professsor;
        this.year = year;
        this.semester = semester;
        this.examType = examType;
    }

    //== setter ==//
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setProfesssor(String professsor) {
        this.professsor = professsor;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }
}
