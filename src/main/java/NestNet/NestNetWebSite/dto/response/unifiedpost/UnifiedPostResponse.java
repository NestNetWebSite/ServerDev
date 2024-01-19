package NestNet.NestNetWebSite.dto.response.unifiedpost;

import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UnifiedPostResponse {

    UnifiedPostDto unifiedPostDto;
    List<AttachedFileDto> fileDtoList;
    List<CommentDto> commentDtoList;
    boolean isMemberLiked;
}
