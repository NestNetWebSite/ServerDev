package NestNet.NestNetWebSite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class JwtAccessTokenDto {

    private String token;
}
