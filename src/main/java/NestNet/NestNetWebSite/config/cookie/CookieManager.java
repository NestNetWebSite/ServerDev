package NestNet.NestNetWebSite.config.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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
    public void setCookie(String name, String value, HttpServletResponse response){

        int expTime = 0;

        if(name.equals("Authorization")){
            expTime = accessTokenExpTime / 1000;
        }
        else{
            expTime = refreshTokenExpTime / 1000;
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expTime);
        cookie.setHttpOnly(true);
//        cookie.setDomain();
        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
