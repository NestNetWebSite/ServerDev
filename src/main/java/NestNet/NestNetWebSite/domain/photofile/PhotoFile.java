package NestNet.NestNetWebSite.domain.photofile;

import NestNet.NestNetWebSite.domain.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.Normalizer;
import java.util.UUID;

/**
 * 사진 게시판 파일 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_file_id")
    private Long id;                                            // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;                                          // 파일이 연관된 게시물

    private String originalFileName;                            // 사용자가 입력한 파일 이름
    private String saveFileName;                                // 실제 저장된 파일 이름
    private String saveFilePath;                                // 파일 저장 경로

    private boolean thumbNail;                                // 썸네일 파일인지

    @Transient
    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    /*
    생성자
     */
    public PhotoFile(Post post, MultipartFile file, boolean thumbNail){
        this.post = post;
        createFileName(file);
        createSavePath();
        this.thumbNail = thumbNail;
    }

    //== 비지니스 로직 ==//

    /*
    썸네일로 지정
     */
    public void setThumbNail(){
        this.thumbNail = true;
    }

    /*
    파일 이름 중복 방지를 위한 파일명 생성
     */
    public void createFileName(MultipartFile file){
        String fileName = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);    //Mac, Window 한글 처리 다른 이슈 처리
        this.originalFileName = fileName;
        this.saveFileName = UUID.randomUUID().toString() + "_" + fileName;

    }

    /*
    파일 저장 경로 생성
     */
    public void createSavePath(){

        StringBuilder folderNameBuilder = new StringBuilder();
        folderNameBuilder.append(this.post.getPostCategory().toString());
        folderNameBuilder.append(File.separator);
        folderNameBuilder.append(this.post.getCreatedTime().getYear());

        if(this.post.getCreatedTime().getMonth().getValue() <= 6){      //상반기 작성된 글
            folderNameBuilder.append("_first_half");
        }
        else{
            folderNameBuilder.append("_second_half");
        }

        String path = folderNameBuilder.toString();    //파일 저장 경로 ( ex) C:/nestnetFile/EXAM/)

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
