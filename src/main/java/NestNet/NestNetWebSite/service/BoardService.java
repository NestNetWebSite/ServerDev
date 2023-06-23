package NestNet.NestNetWebSite.service;

import NestNet.NestNetWebSite.domain.board.Board;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.dto.BoardDto;
import NestNet.NestNetWebSite.dto.ExamCollectionBoardDto;
import NestNet.NestNetWebSite.dto.UnifiedBoardDto;
import NestNet.NestNetWebSite.repository.BoardRepository;
import NestNet.NestNetWebSite.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(ExamCollectionBoardDto boardDto){

        Member findMember = memberRepository.findById(boardDto.getMemberId());
        Board board = boardDto.toEntity(findMember);

        boardRepository.save(board);
    }

    @Transactional
    public void save(UnifiedBoardDto boardDto){

        Member findMember = memberRepository.findById(boardDto.getMemberId());
        Board board = boardDto.toEntity(findMember);

        boardRepository.save(board);
    }
}
