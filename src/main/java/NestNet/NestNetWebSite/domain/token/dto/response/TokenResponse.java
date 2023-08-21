package NestNet.NestNetWebSite.domain.token.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenResponse {

    String accessToken;
    String refreshToken;
}