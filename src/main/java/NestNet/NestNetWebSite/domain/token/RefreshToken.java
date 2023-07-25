package NestNet.NestNetWebSite.domain.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 엑세스 토큰과 매칭되는 리프레시 토큰 정보를 갖고 있는 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh-token-id")
    Long id;                                            // PK

    String accessToken;                                 // Access 토큰

    String refreshToken;                                // Refresh 토큰

    LocalDateTime expTime;                              // Refresh 토큰 만료 시간

    /*
    생성자
     */
    public RefreshToken(String accessToken, String refreshToken, LocalDateTime expTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expTime = expTime;
    }

    //== 비지니스 로직 ==//
    public void changeAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
