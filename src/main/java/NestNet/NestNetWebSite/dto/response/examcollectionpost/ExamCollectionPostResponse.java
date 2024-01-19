package NestNet.NestNetWebSite.dto.response.examcollectionpost;

import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.dto.response.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 족보 게시물 하나에 들어가는 데이터
 */
@Getter
@AllArgsConstructor
public class ExamCollectionPostResponse {

    private ExamCollectionPostDto examCollectionPostDto;
    private List<AttachedFileDto> fileDtoList;
    private List<CommentDto> commentDtoList;
    private boolean isMemberLiked;
}
