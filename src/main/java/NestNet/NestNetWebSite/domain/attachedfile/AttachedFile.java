package NestNet.NestNetWebSite.domain.attachedfile;

import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.Normalizer;
import java.util.UUID;

/**
 * 첨부파일 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class AttachedFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attached_file_id")
    private Long id;                                            // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;                                          // 첨부파일이 포함된 게시물

    private String originalFileName;                            // 사용자가 입력한 파일 이름
    private String saveFileName;                                // 실제 저장된 파일 이름
    private String saveFilePath;                                // 파일 경로 (서버)

//    @Transient      //영속성 컨텍스트에 관리되지 않음
//    @Value("${filePath}")
//    private String filePath;

    /*
    생성자
     */
    public AttachedFile(Post post, MultipartFile file, String baseFilePath){
        this.post = post;
        createFileName(file);
        createSavePath(baseFilePath);
    }

    //== setter ==//
    public void injectPost(Post post) {
        this.post = post;
    }

    //== 비지니스 로직 ==//
    /*
    파일 이름 중복 방지를 위한 파일명 생성
     */
    public void createFileName(MultipartFile file){

        String fileName = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);    //Mac, Window 한글 처리 다른 이슈 처리

        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(UUID.randomUUID().toString()).append("_").append(fileName);

        this.originalFileName = fileName;
        this.saveFileName = fileNameBuilder.toString();
    }

    /*
    파일 저장 경로 생성
     */
    public void createSavePath(String baseFilePath){

        StringBuilder folderBuilder = new StringBuilder();
        folderBuilder.append(this.post.getPostCategory().toString())
                .append(File.separator).append(this.post.getCreatedTime().getYear());

        File folder = new File(baseFilePath + folderBuilder.toString());

        if(!folder.exists()){
            try {
                folder.mkdirs();
            }catch (Exception e){
                throw new CustomException(ErrorCode.CANNOT_SAVE_FILE);
            }
        }

        this.saveFilePath = folderBuilder.toString();
    }

}
