package NestNet.NestNetWebSite.config.jwt;

import NestNet.NestNetWebSite.config.redis.RedisUtil;
import NestNet.NestNetWebSite.service.member.CustomUserDetailsService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private final CustomUserDetailsService customUserDetailsService;
    private final RedisUtil redisUtil;

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
                .claim(AUTHORITIES_KEY, authority)              //권한한
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

        log.info("createRefreshToken :" + authentication.getName() + refreshToken);

        return refreshToken;
    }

    /*
    토큰의 정보를 가져옴
     */
    public Claims getTokenClaims(String accessToken){

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)                 //서버의 시크릿 키로 서명 검증
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        return claims;
    }

    /*
    jwt access 토큰을 이용하여 Authentication 객체 리턴
     */
    public Authentication getAuthentication(String accessToken){

        //토큰의 body의 클레임 정보 ex){sub=admin, auth=admin, exp=1688709600}
        Claims claims = getTokenClaims(accessToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());

        //Authentication 인터페이스의 구현 객체
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /*
   유효한 토큰인지 확인
    */
    public Map<String, Object> validateAccessToken(String accessToken, HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>();

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);

            map.put("accessToken", accessToken);
            map.put("response", response);

            return map;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("TokenProvider.class / validateAccessToken : 잘못된 JWT 서명");

            map.put("accessToken", null);
            map.put("response", response);

            return map;

        } catch (ExpiredJwtException e) {
            log.info("TokenProvider.class / validateAccessToken : 만료된 JWT access 토큰");

            String refreshToken = getRefreshToken(request);

            //리프레쉬 토큰이 유효하면 DB 기존 값 지우고 엑세스 토큰 재발급
            if(refreshToken != null && validateRefreshToken(refreshToken)){

                log.info("TokenProvider.class / validateAccessToken : validateAccessToken 매서드 리프레시 토큰 : " + refreshToken);
                log.info("TokenProvider.class / validateAccessToken : JWT access 토큰 재발급");

                Authentication authentication = getAuthentication(refreshToken);

                String newAccessToken = createAccessToken(authentication);

                response.addHeader(AUTHORIZATION_HEADER, newAccessToken);       //새 발급한 access 토큰 응답헤더에 추가

                map.put("accessToken", newAccessToken);
                map.put("response", response);

                return map;
            }
            else{       //리프레쉬 토큰이 유효하지 않으면 다시 로그인하라는 예외 발생
                log.info("refresh 토큰 만료 / 다시 로그인 해야함");

                map.put("accessToken", null);
                map.put("response", response);

                return map;
            }
        }

    }

    /*
    HTTP 헤더에서 access 토큰 가져옴
     */
    public String resolveToken(HttpServletRequest request){

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        //"Bearer " 부분 슬라이싱. 바로 뒤부터 토큰임
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        if(bearerToken != null){
            return  bearerToken;
        }

        return null;
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
                    System.out.println("최종 : " + refreshToken);
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

}
