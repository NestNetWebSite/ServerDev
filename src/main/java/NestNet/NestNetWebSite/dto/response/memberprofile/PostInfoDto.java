package NestNet.NestNetWebSite.dto.response.memberprofile;

import NestNet.NestNetWebSite.domain.post.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostInfoDto {

    private Long id;
    private String title;
    private PostCategory postCategory;
    private Long viewCount;
    private int likeCount;
}
