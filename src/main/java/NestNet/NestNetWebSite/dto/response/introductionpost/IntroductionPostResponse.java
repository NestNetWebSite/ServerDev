package NestNet.NestNetWebSite.dto.response.introductionpost;

import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class IntroductionPostResponse {

    private IntroductionPostDto introductionPostDto;
    private List<AttachedFileDto> fileDtoList;
    private List<CommentDto> commentDtoList;
    private boolean isMemberLiked;

}
