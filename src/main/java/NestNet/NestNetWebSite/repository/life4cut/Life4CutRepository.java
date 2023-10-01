package NestNet.NestNetWebSite.repository.life4cut;

import NestNet.NestNetWebSite.domain.life4cut.Life4Cut;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class Life4CutRepository {

    private final EntityManager entityManager;
    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    // 저장
    public boolean save(Life4Cut life4Cut, MultipartFile file){
        Path path = Paths.get(basePath + life4Cut.getSaveFilePath() + File.separator + life4Cut.getSaveFileName());
        System.out.println("여기 " + life4Cut.getSaveFileName());
        try {
            file.transferTo(path);
            entityManager.persist(life4Cut);
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //=========================================조회=========================================//
    // id(PK)로 단건 조회
    public Life4Cut findById(Long id){
        return entityManager.find(Life4Cut.class, id);
    }

    // 여러 건 랜덤 페이징 조회
    public List<Life4Cut> findAllByPaging(int limit){

        return entityManager.createNativeQuery("select * from life4cut order by rand() limit " + limit + ";", Life4Cut.class)
                .getResultList();
    }

    //====================================================================================//
}
