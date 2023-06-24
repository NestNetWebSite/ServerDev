package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.board.Board;
import NestNet.NestNetWebSite.domain.board.BoardCategory;
import NestNet.NestNetWebSite.domain.board.BoardType;
import NestNet.NestNetWebSite.domain.board.UnifiedBoard;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager entityManager;

    // 저장
    public void save(Board board){
        entityManager.persist(board);
    }

    // Id(PK)로 단건 조회
    public Board findById(Long id){
        return entityManager.find(Board.class, id);
    }

    // 게시판 종류에 따른 조회 (페이징)
    public List<Board> findAllByBoardCategory(BoardCategory boardCategory, int offset, int limit){

        Map<String, String> boardCategoryMap = new HashMap<>();
        boardCategoryMap.put("자유", "Free");
        boardCategoryMap.put("개발", "Dev");
        boardCategoryMap.put("진로", "Career");
        boardCategoryMap.put("족보", "Exam");
        boardCategoryMap.put("공식문서", "OfficialDoc");
        boardCategoryMap.put("취업정보", "EmploymentInfo");

        List<Board> resultList = entityManager.createNativeQuery(
                "select * from board inner join member" +
                        " on board.member_id = member.member_id" +
                        " where dtype = '" + boardCategoryMap.get(boardCategory) + "';", Board.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    // 통합 게시판(자유 + 개발 + 진로) 모두 조회
    public List<UnifiedBoard> findAllUnifiedBoard(int offset, int limit){

        List<UnifiedBoard> resultList = entityManager.createNativeQuery(
                        "select * from board inner join member" +
                                " on board.member_id = member.member_id" +
                                " inner join unified_board" +
                                " on on board.board_id = unified_board.board_id" +
                                " where dtype = 'Free' and 'Dev' and 'Career';", Board.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    // 통합 게시판 (자유) 조회
    public List<UnifiedBoard> findUnifiedFreeBoard(int offset, int limit){

        List<UnifiedBoard> resultList = entityManager.createNativeQuery(
                        "select * from board inner join member" +
                                " on board.member_id = member.member_id" +
                                " inner join unified_board" +
                                " on on board.board_id = unified_board.board_id" +
                        " where dtype = 'Free';", Board.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    // 통합 게시판 (개발) 조회
    public List<UnifiedBoard> findUnifiedDevBoard(int offset, int limit){

        List<UnifiedBoard> resultList = entityManager.createNativeQuery(
                        "select * from board inner join member" +
                                " on board.member_id = member.member_id" +
                                " inner join unified_board" +
                                " on on board.board_id = unified_board.board_id" +
                        " where dtype = 'Dev';", Board.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    // 통합 게시판 (진로) 조회
    public List<UnifiedBoard> findUnifiedCareerBoard(int offset, int limit){

        List<UnifiedBoard> resultList = entityManager.createNativeQuery(
                        "select * from board inner join member" +
                                " on board.member_id = member.member_id" +
                                " inner join unified_board" +
                                " on on board.board_id = unified_board.board_id" +
                        " where dtype = 'Career';", Board.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }
}
