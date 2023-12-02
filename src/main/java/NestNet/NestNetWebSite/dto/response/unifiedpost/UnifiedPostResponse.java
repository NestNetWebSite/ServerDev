package NestNet.NestNetWebSite.dto.response.unifiedpost;

import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UnifiedPostResponse {

    UnifiedPostDto unifiedPostDto;
    List<AttachedFileResponse> fileDtoList;
    List<CommentResponse> commentResponseList;
    boolean isMemberLiked;
}