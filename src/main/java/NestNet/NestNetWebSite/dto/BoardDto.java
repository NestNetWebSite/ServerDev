package NestNet.NestNetWebSite.dto;

import NestNet.NestNetWebSite.domain.board.Board;
import NestNet.NestNetWebSite.domain.member.Member;

public interface BoardDto {

    //== DTO ---> Entity ==//
    Board toEntity(Member member);
}
