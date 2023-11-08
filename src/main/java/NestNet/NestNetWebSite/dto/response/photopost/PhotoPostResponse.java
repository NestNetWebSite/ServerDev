package NestNet.NestNetWebSite.dto.response.photopost;

import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PhotoPostResponse {

    PhotoPostDto photoPostDto;
    List<AttachedFileResponse> fileDtoList;
    List<CommentResponse> commentResponseList;
    boolean isMemberLiked;
}
