package NestNet.NestNetWebSite.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }


}
