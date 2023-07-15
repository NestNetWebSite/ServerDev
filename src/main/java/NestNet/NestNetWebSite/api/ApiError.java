package NestNet.NestNetWebSite.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {

    private final int status;
    private final String message;

    public ApiError(int status, String message){
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, Throwable throwable){
        this(status.value(), throwable.getMessage());               // RequiredArgsConstructor 호출
    }

}
