package NestNet.NestNetWebSite.domain.post;

import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Exam")
@Getter
public class ExamCollectionPost extends Post{

    private String subject;                                             // 과목

    private String professsor;                                          // 교수

    private int year;                                                // 출제 년도

    private int semester;                                            // 출제 학기

    @Enumerated(EnumType.STRING)
    private ExamType examType;                                          // 중간/기말 분류

//    @OneToMany(mappedBy = "examCollectionPost", cascade = CascadeType.ALL)
//    private List<AttachedFile> attachedFileList = new ArrayList<>();    // 첨부파일

    protected ExamCollectionPost(){}

    @Builder
    public ExamCollectionPost(String title, String bodyContent, Member member, int viewCount, int recommendationCount, PostCategory postCategory,
                               LocalDateTime createdTime, String subject, String professsor, int year, int semester, ExamType examType) {

        super(title, bodyContent, member, viewCount, recommendationCount, postCategory, createdTime);
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
