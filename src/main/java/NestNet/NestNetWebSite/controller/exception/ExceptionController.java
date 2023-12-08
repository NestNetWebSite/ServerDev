package NestNet.NestNetWebSite.controller.exception;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e){

        log.error(e.getErrorCode().getMsg(), e.getStackTrace()[0]);        // 인덱스 0 -> 실제 예외 발생 지점

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ApiResult.error(e.getErrorCode().getHttpStatus(), e.getErrorCode().getMsg()));
    }

}
