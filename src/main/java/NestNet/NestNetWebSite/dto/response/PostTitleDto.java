package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostTitleDto {

    private Long id;
    private String title;
    private PostCategory postCategory;
}
