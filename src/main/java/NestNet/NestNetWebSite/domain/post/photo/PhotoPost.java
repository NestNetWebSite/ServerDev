package NestNet.NestNetWebSite.domain.post.photo;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.photofile.PhotoFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.PostCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 족보(기출) 게시물 엔티티
 */
@Entity
@Getter
@DiscriminatorValue("Photo")
@NoArgsConstructor
public class PhotoPost extends Post {

    @OneToMany(mappedBy = "post")
    private List<PhotoFile> photoFileList = new ArrayList<>();

    /*
    생성자
     */
    @Builder
    public PhotoPost(String title, String bodyContent, Member member, Long viewCount, int recommendationCount, LocalDateTime createdTime) {

        super(title, bodyContent, member, viewCount, recommendationCount, PostCategory.PHOTO, createdTime);
    }

    /*
    연관관계 편의 매서드
     */
    public void setPhotoFileList(List<PhotoFile> photoFiles){

//        // 리스트에 같은 객체가 있으면 삭제
//        if(photoFileList.contains(photoFile)){
//            photoFileList.remove(photoFile);
//        }
//        this.photoFileList.add(photoFile);
//        photoFile.setPost(this);

        this.photoFileList = photoFiles;
    }

    //== 비지니스 로직 ==//
    /*
    게시글 수정
     */
    public void modifyPost(String title, String bodyContent) {

        super.setTitle(title);
        super.setBodyContent(bodyContent);
        super.setModifiedTime(LocalDateTime.now());
    }
}
