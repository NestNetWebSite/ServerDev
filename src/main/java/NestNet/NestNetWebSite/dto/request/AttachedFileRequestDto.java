package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.PostCategory;
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
    private PostCategory postCategory;

    public AttachedFileRequestDto(Long PostId, MultipartFile file, PostCategory postCategory) {
        this.PostId = PostId;
        this.file = file;
        this.postCategory = postCategory;
    }

    //== DTO ---> Entity ==//
    public AttachedFile toEntity(Post post){      //연관관계 가 있는 엔티티는 id를 이용해 찾고 주입

        AttachedFile attachedFile = new AttachedFile(post, file);
        return attachedFile;
    }

}
