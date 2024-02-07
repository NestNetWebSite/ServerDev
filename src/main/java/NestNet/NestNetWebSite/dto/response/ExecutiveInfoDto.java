package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveInfoDto {

    private Long id;
    private int year;
    private String name;
    private String studentId;
    private String role;
    private int priority;
}
