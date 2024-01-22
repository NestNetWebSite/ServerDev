package NestNet.NestNetWebSite.dto.response.noticepost;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class NoticePostListResponse {

    Long totalSize;
    List<NoticePostListDto> dtoList = new ArrayList<>();
}
