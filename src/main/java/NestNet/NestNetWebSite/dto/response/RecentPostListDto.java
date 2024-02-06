package NestNet.NestNetWebSite.dto.response;

import NestNet.NestNetWebSite.domain.post.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecentPostListDto {

    private Long id;
    private PostCategory postCategory;
    private String title;
    private LocalDateTime createdTime;
}
