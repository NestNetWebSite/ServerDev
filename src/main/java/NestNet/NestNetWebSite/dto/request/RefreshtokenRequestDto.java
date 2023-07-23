package NestNet.NestNetWebSite.dto.request;

import NestNet.NestNetWebSite.domain.token.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshtokenRequestDto {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime expTime;

    //== DTO ---> Entity ==//
    public RefreshToken toEntity(){

        return RefreshToken.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .expTime(this.expTime)
                .build();
    }
}
