package NestNet.NestNetWebSite.domain.life4cut;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * 인생네컷 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class Life4Cut {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "life_4_cut_id")
    private Long id;

    private String saveFilePath;
    private String saveFileName;

    @Transient      //영속성 컨텍스트에 관리되지 않음
    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    /*
    생성자
     */
    public Life4Cut(MultipartFile file){
        createFileName(file);
        createSavePath();
    }

    //== 비지니스 로직 ==//
    /*
    파일 이름 중복 방지를 위한 파일명 생성
     */
    public void createFileName(MultipartFile file){
        this.saveFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    }

    /*
    파일 저장 경로 생성
     */
    public void createSavePath(){

        String path = "LIFE4CUT";

        File folder = new File(basePath + path);               //해당 경로에 폴더 생성

        if(!folder.exists()){       //해당 폴더가 존재하지 않을 경우 생성
            try {
                folder.mkdir();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        this.saveFilePath = path;
    }

}
