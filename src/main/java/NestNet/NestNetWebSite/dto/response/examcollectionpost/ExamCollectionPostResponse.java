package NestNet.NestNetWebSite.dto.response.examcollectionpost;

import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 족보 게시물 하나에 들어가는 데이터
 */
@Getter
@AllArgsConstructor
public class ExamCollectionPostResponse {

    ExamCollectionPostDto examCollectionPostDto;
    List<AttachedFileResponse> fileDtoList;
    List<CommentResponse> commentResponseList;
    boolean isMemberLiked;
}
