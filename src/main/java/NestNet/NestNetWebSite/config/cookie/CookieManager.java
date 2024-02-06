package NestNet.NestNetWebSite.config.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    @Value("#{environment['jwt.access-exp-time']}")
    private int accessTokenExpTime;

    @Value("#{environment['jwt.refresh-exp-time']}")
    private int refreshTokenExpTime;

    /*
    response에 쿠키를 만들어 Add
     */
    public void setCookie(String name, String value, boolean isLogout, HttpServletResponse response){

        int expTime = 0;

        if(!isLogout && name.equals("Authorization")){
            expTime = accessTokenExpTime / 1000;
        }
        else if(!isLogout && name.equals("refresh-token")){
            expTime = refreshTokenExpTime / 1000;
        }

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
//                .domain("192.168.219.114")
                .maxAge(expTime)
                .httpOnly(false)
                .secure(false)
//                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

    }
}
