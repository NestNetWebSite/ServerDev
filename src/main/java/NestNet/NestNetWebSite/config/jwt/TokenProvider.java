package NestNet.NestNetWebSite.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    @Value("#{environment['jwt.secret']}")
    private String secret;                                      // 시크릿 키
    @Value("#{environment['jwt.access-exp-time']}")
    private long accessTokenExpTime;                            // access 토큰 유효 기간
    @Value("#{environment['jwt.refresh-exp-time']}")
    private long refreshTokenExpTime;                           // refresh 토큰 유효 기간
    private Key key;
    private final RedisTemplate<String, String> redisTemplate;

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

        Date now = new Date();
        Date validity = new Date(now.getTime() + this.accessTokenExpTime);    //현재시간 + 토큰 유효 시간 == 만료날짜

        String authority = null;
        if (authentication.getAuthorities().size() > 0){            //권한이 있을 경우
            authority = authentication.getAuthorities().iterator().next().getAuthority();
        }

        return Jwts.builder()
                .setSubject(authentication.getName())           //로그인 아이디
                .claim(AUTHORITIES_KEY, authority)
                .setIssuedAt(now)                               //생성날짜
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /*
    로그인한 사용자의 Authentication를 이용하여 refresh 토큰 발급
     */
    public String createRefreshToken(Authentication authentication){

        Date now = new Date();
        Date validity = new Date(now.getTime() + this.refreshTokenExpTime);    //현재시간 + 토큰 유효 시간 == 만료날짜

        String authority = null;
        if (authentication.getAuthorities().size() > 0){            //권한이 있을 경우
            authority = authentication.getAuthorities().iterator().next().getAuthority();
        }

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authority)
                .setIssuedAt(now)                               //생성날짜
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // redis에 저장
        redisTemplate.opsForValue().set(
                authentication.getName(),           // 로그인 아이디
                refreshToken,
                refreshTokenExpTime,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    /*
    jwt access 토큰을 이용하여 Authentication 객체 리턴
     */
    public Authentication getAuthentication(String accessToken){

        //토큰의 body의 클레임 정보 ex){sub=admin, auth=admin, exp=1688709600}
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)                 //서버의 시크릿 키로 서명 검증
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        //권한 정보 가져오기 ex) VICE_PRESIDENT
        Collection<? extends GrantedAuthority> authority =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))     //claims.get(AUTHORITIES_KEY).toString().split(",")
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authority);

        //Authentication 인터페이스의 구현 객체
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authority);
    }


    /*
   유효한 토큰인지 확인
    */
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
