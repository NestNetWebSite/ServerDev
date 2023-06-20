package NestNet.NestNetWebSite.domain.board;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@DiscriminatorValue("u")
@Getter
public class UnifiedBoard extends Board{

    @Enumerated(value = EnumType.STRING)
    private BoardType boardType;                    // 게시판 소분류 (자유, 개발, 진로)

    //== setter ==//
    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
    }
}
