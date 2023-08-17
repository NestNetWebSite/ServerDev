package NestNet.NestNetWebSite.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberModifyInfoRequestDto {

    String loginId;
    String name;
    String studentId;
    int grade;
    String emailAddress;
}
