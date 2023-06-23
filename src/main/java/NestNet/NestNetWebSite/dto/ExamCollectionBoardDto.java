package NestNet.NestNetWebSite.dto;

import NestNet.NestNetWebSite.domain.board.Board;
import NestNet.NestNetWebSite.domain.board.BoardCategory;
import NestNet.NestNetWebSite.domain.board.ExamCollectionBoard;
import NestNet.NestNetWebSite.domain.board.ExamType;
import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExamCollectionBoardDto implements BoardDto{

    private Long memberId;
    private String title;
    private String bodyContent;
    private BoardCategory boardCategory;
    private String subject;
    private String professsor;
    private int year;
    private int semester;
    private ExamType examType;


    public ExamCollectionBoardDto(Long memberId, String title, String bodyContent, BoardCategory boardCategory, String subject, String professsor,
                                  int year, int semester, ExamType examType) {

        this.memberId = memberId;
        this.title = title;
        this.bodyContent = bodyContent;
        this.boardCategory = boardCategory;
        this.subject = subject;
        this.professsor = professsor;
        this.year = year;
        this.semester = semester;
        this.examType = examType;
    }

    //== DTO ---> Entity ==//
    public Board toEntity(Member member){

        return ExamCollectionBoard.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0)
                .recommendationCount(0)
                .boardCategory(this.boardCategory)
                .createdTime(LocalDateTime.now())
                .subject(this.subject)
                .professsor(this.professsor)
                .year(this.year)
                .semester(this.semester)
                .examType(this.examType)
                .build();
    }
}
