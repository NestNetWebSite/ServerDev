package NestNet.NestNetWebSite.domain.post.notice;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.PostCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("Notice")
@NoArgsConstructor
public class NoticePost extends Post {

    /*
    생성자
     */
    @Builder
    public NoticePost(String title, String bodyContent, Member member, Long viewCount, int recommendationCount, LocalDateTime createdTime){

        super(title, bodyContent, member, viewCount, recommendationCount, PostCategory.NOTICE, createdTime);
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
