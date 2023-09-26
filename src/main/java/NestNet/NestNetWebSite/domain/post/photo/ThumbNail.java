package NestNet.NestNetWebSite.domain.post.photo;

import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.PostCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.Normalizer;
import java.util.UUID;

/**
 * 사진 게시판 썸네일 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class ThumbNail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thumb_nail_id")
    private Long id;                                            // PK

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;                                          // 썸네일이 해당되는 게시물

//    private String title;                                       // 게시물 제목
    private String saveFileName;                                // 실제 저장된 파일 이름
    private String saveFilePath;                                // 파일 경로 (서버)

    @Transient      //영속성 컨텍스트에 관리되지 않음
    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    /*
    생성자
     */
    public ThumbNail(Post post, MultipartFile file){
        this.post = post;
        createFileName(file);
        createSavePath();
    }

    //== 비지니스 로직 ==//
    /*
    파일 이름 중복 방지를 위한 파일명 생성
     */
    public void createFileName(MultipartFile file){

        String fileName = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);    //Mac, Window 한글 처리 다른 이슈 처리
        this.saveFileName = UUID.randomUUID().toString() + "_" + fileName;
    }

    /*
    파일 저장 경로 생성
     */
    public void createSavePath(){

        String path = "PHOTO_POST_THUMBNAIL";

        File folder = new File(basePath + path);               //파일 저장 경로 ( C:/nestnetFile/PHOTO_POST_THUMBNAIL)

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
