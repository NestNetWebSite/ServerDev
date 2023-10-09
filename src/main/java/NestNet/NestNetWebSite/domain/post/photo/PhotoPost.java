package NestNet.NestNetWebSite.domain.post.photo;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.PostCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 족보(기출) 게시물 엔티티
 */
@Entity
@Getter
@DiscriminatorValue("Photo")
@NoArgsConstructor
public class PhotoPost extends Post {

    /*
    생성자
     */
    @Builder
    public PhotoPost(String title, String bodyContent, Member member, Long viewCount, int recommendationCount, LocalDateTime createdTime) {

        super(title, bodyContent, member, viewCount, recommendationCount, PostCategory.PHOTO, createdTime);
    }

    public void modifyPost(String title, String bodyContent) {
        // 사진 넣었다가 빼는 경우는 어떻게 처리??
    }
}
