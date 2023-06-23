package NestNet.NestNetWebSite.domain.board;

import NestNet.NestNetWebSite.domain.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Unified")
@Getter
public class UnifiedBoard extends Board{

    @Enumerated(value = EnumType.STRING)
    private BoardType boardType;                    // 게시판 소분류 (자유, 개발, 진로)

    protected UnifiedBoard() {}

    @Builder
    public UnifiedBoard(String title, String bodyContent, Member member, int viewCount, int recommendationCount, BoardCategory boardCategory,
                        LocalDateTime createdTime, BoardType boardType){

        super(title, bodyContent, member, viewCount, recommendationCount, boardCategory, createdTime);
        this.boardType = boardType;
    }


    //== setter ==//
    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
    }
}
