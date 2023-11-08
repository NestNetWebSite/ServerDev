package NestNet.NestNetWebSite.dto.response.memberprofile;

import NestNet.NestNetWebSite.domain.post.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostInfoResponse {

    List<PostInfoDto> dtoList = new ArrayList<>();

}
