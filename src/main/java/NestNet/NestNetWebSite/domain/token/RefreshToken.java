package NestNet.NestNetWebSite.domain.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh-token-id")
    Long id;

    String accessToken;

    String refreshToken;

    LocalDateTime expTime;      //만료 시간

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
