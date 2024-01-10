package NestNet.NestNetWebSite.dto.response.photopost;

import NestNet.NestNetWebSite.dto.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PhotoPostResponse {

    PhotoPostDto photoPostDto;
    List<PhotoFileDto> fileDtoList;
    List<CommentResponse> commentResponseList;
    boolean isMemberLiked;
}
