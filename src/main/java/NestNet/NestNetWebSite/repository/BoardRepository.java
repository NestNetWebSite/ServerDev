package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.board.Board;
import NestNet.NestNetWebSite.domain.board.BoardType;
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
    public List<Board> findAllByBoardType(String boardType, int offset, int limit){
        Map<String, String> boardTypeMap = new HashMap<>();
        boardTypeMap.put("자유", "Free");
        boardTypeMap.put("개발", "Dev");
        boardTypeMap.put("진로", "Career");
        boardTypeMap.put("족보", "Exam");
        boardTypeMap.put("공식문서", "OfficialDoc");
        boardTypeMap.put("취업정보", "EmploymentInfo");
        List<Board> resultList = entityManager.createNativeQuery(
                "select * from board inner join member" +
                        " on board.member_id = member.member_id" +
                        " where dtype = '" + boardTypeMap.get(boardType) + "';", Board.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }
}
