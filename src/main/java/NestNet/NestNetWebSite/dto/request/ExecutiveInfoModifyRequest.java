package NestNet.NestNetWebSite.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveInfoModifyRequest {

    private Long id;
    private int year;
    private String name;
    private String studentId;
    private String role;
}
