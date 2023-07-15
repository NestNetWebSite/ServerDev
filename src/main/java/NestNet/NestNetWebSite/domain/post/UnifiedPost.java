package NestNet.NestNetWebSite.domain.post;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.PostCategory;
import NestNet.NestNetWebSite.domain.post.PostType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Unified")
@Getter
public class UnifiedPost extends Post {

    @Enumerated(value = EnumType.STRING)
    private PostType postType;                    // 게시판 소분류 (자유, 개발, 진로)

    protected UnifiedPost() {}

    @Builder
    public UnifiedPost(String title, String bodyContent, Member member, int viewCount, int recommendationCount, PostCategory postCategory,
                        LocalDateTime createdTime, PostType postType){

        super(title, bodyContent, member, viewCount, recommendationCount, postCategory, createdTime);
        this.postType = postType;
    }


    //== setter ==//
    public void setPostType(PostType postType) {
        this.postType = postType;
    }
}
