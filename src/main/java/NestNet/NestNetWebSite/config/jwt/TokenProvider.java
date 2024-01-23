package NestNet.NestNetWebSite.config.jwt;

import NestNet.NestNetWebSite.config.auth.Authenticator;
import NestNet.NestNetWebSite.config.cookie.CookieManager;
import NestNet.NestNetWebSite.config.redis.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private final Authenticator authenticator;
    private final RedisUtil redisUtil;

    private final CookieManager cookieManager;

    @Value("#{environment['jwt.secret']}")
    private String secret;                                      // 시크릿 키
    @Value("#{environment['jwt.access-exp-time']}")
    private long accessTokenExpTime;                            // access 토큰 유효 기간
    @Value("#{environment['jwt.refresh-exp-time']}")
    private long refreshTokenExpTime;                           // refresh 토큰 유효 기간

    private static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private Key key;

    /*
   빈이 생성되고 의존관계 주입까지 완료된 후, Key 변수에 값 할당
    */
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);     //생성자 주입으로 받은 secret 값을 Base64에 디코딩하여 key 변수에 할당
        this.key = Keys.hmacShaKeyFor(keyBytes);              //hmac 알고리즘을 이용하여 Key 인스턴스 생성
    }

    /*
    로그인한 사용자의 Authentication를 이용하여 access 토큰 발급
     */
    public String createAccessToken(Authentication authentication){

        Date validity = new Date(System.currentTimeMillis() + accessTokenExpTime);    //현재시간 + 토큰 유효 시간 == 만료날짜
        //권한 가져옴
        String authority = null;
        if (authentication.getAuthorities().size() > 0){            //권한이 있을 경우
            authority = authentication.getAuthorities().iterator().next().getAuthority();
        }

        //엑세스 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName())           //로그인 아이디
                .claim(AUTHORITIES_KEY, authority)              //권한
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /*
    로그인한 사용자의 Authentication를 이용하여 refresh 토큰 발급
     */
    public String createRefreshToken(Authentication authentication){

        Date validity = new Date(System.currentTimeMillis() + this.refreshTokenExpTime);    //현재시간 + 토큰 유효 시간 == 만료날짜

        //권한 가져옴
        String authority = null;
        if (authentication.getAuthorities().size() > 0){            //권한이 있을 경우
            authority = authentication.getAuthorities().iterator().next().getAuthority();
        }

        //리프레쉬 토큰 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authority)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return refreshToken;
    }

    /*
    토큰에서 claim 정보 추출
     */
    public Claims getTokenClaims(String token){

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    /*
    jwt 토큰을 이용하여 Authentication 객체 리턴
     */
    public Authentication getAuthentication(String accessToken){

        return authenticator.createAuthentication(getTokenClaims(accessToken).getSubject());
    }

    /*
   유효한 토큰인지 확인
    */
    public String validateAccessToken(String accessToken, HttpServletRequest request, HttpServletResponse response) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);

            return accessToken;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("TokenProvider.class / validateAccessToken : 잘못된 JWT 서명");

            return null;

        } catch (ExpiredJwtException e) {
            log.info("TokenProvider.class / validateAccessToken : 만료된 JWT access 토큰");

            String refreshToken = getRefreshToken(request);

            //리프레쉬 토큰이 유효하면 DB 기존 값 지우고 엑세스 토큰 재발급
            if(refreshToken != null && validateRefreshToken(refreshToken)){

                log.info("TokenProvider.class / validateAccessToken : JWT access 토큰 재발급");

                String newAccessToken = createNewAccessToken(refreshToken, response);

                return newAccessToken;
            }
            else{       //리프레쉬 토큰이 유효하지 않으면 다시 로그인하라는 예외 발생
                log.info("refresh 토큰 만료 / 다시 로그인 해야함");

                return null;
            }
        }

    }

    /*
    HTTP 헤더에서 access 토큰 가져옴
     */
    public String resolveToken(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();

        String accessToken = null;

        if (cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("Authorization")){
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        //"Bearer " 부분 슬라이싱. 바로 뒤부터 토큰임
//        if(accessToken != null && accessToken.startsWith("Bearer ")){
//            accessToken = accessToken.substring(7);
//        }

        return accessToken;
    }

    /*
    HTTP 요청의 쿠키에서 refresh 토큰 가져옴
     */
    public String getRefreshToken(HttpServletRequest request){

        String refreshToken = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("refresh-token")){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if(refreshToken == null){
            log.info("TokenProvider.class / getRefreshToken : 쿠키에 refresh-token이 없음");
        }

        return refreshToken;
    }

    /*
    HTTP 요청에서 받은 refreshToken이 유효한지 판정
     */
    public boolean validateRefreshToken(String requestRefreshToken){

        //쿠키에서 받은 리프레시 토큰이 없음
        if(requestRefreshToken == null){
            log.info("TokenProvider.class / validateRefreshToken : refresh 토큰 없음");
            return false;
        }

        // 레디스에 리프레시 토큰이 있으면
        if(redisUtil.hasKey(requestRefreshToken) && redisUtil.getData(requestRefreshToken).equals("refresh-token")){
            log.info("TokenProvider.class / validateRefreshToken : refresh 토큰이 유효함");
            return true;
        }

        return false;
    }

    /*
    refresh 토큰으로 새로운 access 토큰 발급 & 쿠키 세팅
     */
    public String createNewAccessToken(String refreshToken, HttpServletResponse response){

        Authentication authentication = getAuthentication(refreshToken);

        String newAccessToken = createAccessToken(authentication);

        cookieManager.setCookie("Authorization", newAccessToken, response);

        return newAccessToken;
    }

    /*
    access 토큰 폐기 (블랙리스트 등록)
     */
    public void invalidateAccessToken(HttpServletRequest request){

        String accessToken = resolveToken(request);

        Claims claims = getTokenClaims(accessToken);

        long expTime = claims.getExpiration().getTime();     //토큰의 만료 시각
        long now = new Date().getTime();                     //현재 시각

        // 남은 엑세스토큰 유효 시간
        long remainTime = expTime - now;        // 테스트 사이트 : https://currentmillis.com/

        //레디스에 블랙리스트 등록
        redisUtil.setData(accessToken, "logout", remainTime);
    }

    /*
    refresh 토큰 폐기
     */
    public void invalidateRefreshToken(HttpServletRequest request){

        String refreshToken = getRefreshToken(request);

        if(refreshToken != null){
            redisUtil.deleteData(refreshToken);
        }
    }

}
