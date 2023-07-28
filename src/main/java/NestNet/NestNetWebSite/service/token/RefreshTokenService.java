package NestNet.NestNetWebSite.service.token;

import NestNet.NestNetWebSite.domain.token.RefreshToken;
import NestNet.NestNetWebSite.dto.request.RefreshtokenRequestDto;
import NestNet.NestNetWebSite.dto.response.RefreshTokenDto;
import NestNet.NestNetWebSite.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /*
    리프레쉬 토큰 저장
     */
    @Transactional
    public void save(RefreshtokenRequestDto refreshtokenRequestDto){
        refreshTokenRepository.save(refreshtokenRequestDto.toEntity());
    }

    /*
    엑세스 토큰으로 리프레쉬 토큰 찾고, 만약 만료됐으면 RefreshToken 삭제
     */
    @Transactional
    public RefreshTokenDto findByAccessToken(String accessToken){

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(null);

        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken);
        LocalDateTime now = LocalDateTime.now();

        if(refreshToken.getExpTime().isAfter(now)){     //리프레쉬 토큰이 만료되지 않았으면
            refreshTokenDto.setRefreshToken(refreshToken.getRefreshToken());
        }
        else{       //리프레쉬 토큰이 만료된 경우
            log.info("RefreshTokenService / findByAccessToken : 리프리쉐 토큰 만료");
            deleteRefreshToken(refreshToken);
        }

        return refreshTokenDto;
    }

    /*
    리프리세 토큰 삭제
     */
    @Transactional
    public void deleteRefreshToken(RefreshToken refreshToken){

        int delRowNum = refreshTokenRepository.delete(refreshToken.getId());
        log.info("RefreshTokenService / deleteRefreshToken : " + delRowNum + "개 행 삭제 ");
    }

    /*
    리프레시 토큰 업데이트
     */
    @Transactional
    public void updateRefreshToken(String oldAccessToken, String newAccessToken){

        RefreshToken findRefreshToken = refreshTokenRepository.findByAccessToken(oldAccessToken);

        findRefreshToken.changeAccessToken(newAccessToken);        //더티 체킹에 의해 DB 반영

    }
}
