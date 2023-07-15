package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.post.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.Normalizer;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class AttachedFileRequestDto {

    private MultipartFile file;
    private Long PostId;
    private String folderName;                  //폴더명. ex) 족보, 공식문서, 자유게시판, 개발게시판, 진로게시판...
    private String originalFileName;
    private String saveFileName;
    private String saveFilePath;
    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    public AttachedFileRequestDto(Long PostId, MultipartFile file, String folderName) {
        this.PostId = PostId;
        this.file = file;
        this.folderName = folderName;

        createFileName();
        createSavePath();
    }

    //== DTO ---> Entity ==//
    public AttachedFile toEntity(Post post){      //연관관계 가 있는 엔티티는 id를 이용해 찾고 주입
        return AttachedFile.builder()
                .post(post)
                .originalFileName(this.originalFileName)
                .saveFileName(this.saveFileName)
                .saveFilePath(this.saveFilePath)
                .build();
    }

    //파일 이름 중복 방지를 위한 파일명 생성
    public void createFileName(){
        String fileName = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);    //Mac, Window 한글 처리 다른 이슈 처리
        this.originalFileName = fileName;
        this.saveFileName = UUID.randomUUID().toString() + "_" + fileName;

    }

    //파일 저장 경로 생성 (
    public void createSavePath(){
        String path = basePath + folderName;
        File folder = new File(path);

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
