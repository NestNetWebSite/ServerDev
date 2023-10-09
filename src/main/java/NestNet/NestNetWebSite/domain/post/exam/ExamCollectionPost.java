package NestNet.NestNetWebSite.domain.post.exam;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.PostCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 족보(기출) 게시물 엔티티
 */
@Entity
@Getter
@DiscriminatorValue("Exam")
@NoArgsConstructor
public class ExamCollectionPost extends Post {

    private String subject;                                             // 과목

    private String professor;                                          // 교수

    private int year;                                                   // 출제 년도

    private int semester;                                               // 출제 학기

    @Enumerated(EnumType.STRING)
    private ExamType examType;                                          // 중간/기말  MID / FINAL

    /*
    생성자
     */
    @Builder
    public ExamCollectionPost(String title, String bodyContent, Member member, Long viewCount, int recommendationCount,
                               LocalDateTime createdTime, String subject, String professor, int year, int semester, ExamType examType) {

        super(title, bodyContent, member, viewCount, recommendationCount, PostCategory.EXAM, createdTime);
        this.subject = subject;
        this.professor = professor;
        this.year = year;
        this.semester = semester;
        this.examType = examType;
    }

    //== 비지니스 로직 ==//
    /*
    게시글 수정
     */
    public void modifyPost(String title, String bodyContent, String subject, String professor, int year, int semester, ExamType examType) {

        super.setTitle(title);
        super.setBodyContent(bodyContent);
        super.setModifiedTime(LocalDateTime.now());
        this.subject = subject;
        this.professor = professor;
        this.year = year;
        this.semester = semester;
        this.examType = examType;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
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
