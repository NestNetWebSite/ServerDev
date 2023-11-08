package NestNet.NestNetWebSite.dto.response.examcollectionpost;

import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 족보 리스트에 들어가는 데이터
 */
@Getter
@AllArgsConstructor
public class ExamCollectionPostListResponse {

    Long totalSize;
    List<ExamCollectionPostListDto> dtoList = new ArrayList<>();
}
