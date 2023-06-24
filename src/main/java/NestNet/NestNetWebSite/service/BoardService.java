package NestNet.NestNetWebSite.service;

import NestNet.NestNetWebSite.domain.board.Board;
import NestNet.NestNetWebSite.domain.board.BoardCategory;
import NestNet.NestNetWebSite.domain.board.BoardType;
import NestNet.NestNetWebSite.domain.board.UnifiedBoard;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.dto.BoardDto;
import NestNet.NestNetWebSite.dto.ExamCollectionBoardDto;
import NestNet.NestNetWebSite.dto.UnifiedBoardDto;
import NestNet.NestNetWebSite.repository.BoardRepository;
import NestNet.NestNetWebSite.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // Create
    @Transactional
    public void save(ExamCollectionBoardDto boardDto) {

        Member findMember = memberRepository.findById(boardDto.getMemberId());
        Board board = boardDto.toEntity(findMember);

        boardRepository.save(board);
    }

    // Create
    @Transactional
    public void save(UnifiedBoardDto boardDto) {

        Member findMember = memberRepository.findById(boardDto.getMemberId());
        Board board = boardDto.toEntity(findMember);

        boardRepository.save(board);
    }

    // Read     ---> dtype으로 모든 게시판 필터링 가능
//    public List<BoardDto> findAllByBoardCategory(BoardCategory boardCategory, int offset, int limit){
//        List<Board> boardList = boardRepository.findAllByBoardCategory(boardCategory, offset, limit);
//        List<BoardDto> boardDtos = new ArrayList<>();
//        if(boardCategory.equals("족보")){
//            for(Board board : boardList){
//                ExamCollectionBoardDto(board.getMember().getId(), board.getTitle(), board.getBodyContent(), board.getBoardCategory(),)
//            }
//        }
//
//    }

    // Read ---> 통합 게시판 조회
    public List<UnifiedBoardDto> findAllFromUnifiedBoard(BoardType boardType, int offset, int limit) {

        List<UnifiedBoard> boardList = new ArrayList<>();

        if (boardType.equals("자유")) {
            boardList = boardRepository.findUnifiedFreeBoard(offset, limit);
        } else if (boardType.equals("개발")) {
            boardList = boardRepository.findUnifiedDevBoard(offset, limit);
        } else if (boardType.equals("진로")) {
            boardList = boardRepository.findUnifiedCareerBoard(offset, limit);
        }

        List<UnifiedBoardDto> boardDtos = new ArrayList<>();

        for (UnifiedBoard board : boardList) {
            boardDtos.add(new UnifiedBoardDto(board.getMember().getId(), board.getTitle(), board.getBodyContent(), board.getBoardCategory(), board.getBoardType()));
        }

        return boardDtos;
    }
}
