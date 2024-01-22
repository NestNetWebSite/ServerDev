package NestNet.NestNetWebSite.dto.response.noticepost;

import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NoticePostResponse {

    NoticePostDto noticePostDto;
    List<AttachedFileDto> fileDtoList;
    List<CommentDto> commentDtoList;
    boolean isMemberLiked;
}
