package NestNet.NestNetWebSite.dto;

import NestNet.NestNetWebSite.domain.board.*;
import NestNet.NestNetWebSite.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UnifiedBoardDto implements BoardDto{

    private Long memberId;
    private String title;
    private String bodyContent;
    private BoardCategory boardCategory;
    private BoardType boardType;                    // 게시판 소분류 (자유, 개발, 진로)

    public UnifiedBoardDto(Long memberId, String title, String bodyContent, BoardCategory boardCategory, BoardType boardType) {
        this.memberId = memberId;
        this.title = title;
        this.bodyContent = bodyContent;
        this.boardCategory = boardCategory;
        this.boardType = boardType;
    }

    //== DTO ---> Entity ==//
    public Board toEntity(Member member){

        return UnifiedBoard.builder()
                .title(this.title)
                .bodyContent(this.bodyContent)
                .member(member)
                .viewCount(0)
                .recommendationCount(0)
                .boardCategory(this.boardCategory)
                .createdTime(LocalDateTime.now())
                .boardType(this.boardType)
                .build();
    }
}
