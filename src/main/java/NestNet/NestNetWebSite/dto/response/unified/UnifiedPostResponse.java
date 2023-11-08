package NestNet.NestNetWebSite.dto.response.unified;

import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class UnifiedPostResponse {

    UnifiedPostDto unifiedPostDto;
    List<AttachedFileResponse> fileDtoList;
    List<CommentResponse> commentResponseList;
    boolean isMemberLiked;
}
