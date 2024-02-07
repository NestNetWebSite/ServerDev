package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.executive.ExecutiveInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveInfoRequest {

    private int year;
    private String name;
    private String studentId;
    private String role;

    //== DTO ---> Entity ==//
    public ExecutiveInfo toEntity(){

        return new ExecutiveInfo(year, name, studentId, role, 0);
    }
}
