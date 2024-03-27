package NestNet.NestNetWebSite.controller.exception;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.service.slack.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionController {

    private final SlackService slackService;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e, HttpServletRequest request){

        // 500 에러의 경우 즉시 슬랙으로 전송
        if(e.getErrorCode().getHttpStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
            slackService.sendSlackErrorLog(e, request);
        }

        log.error(e.getErrorCode().getMsg(), e.getStackTrace()[0]);        // 인덱스 0 -> 실제 예외 발생 지점

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ApiResult.error(e.getErrorCode().getHttpStatus(), e.getErrorCode().getMsg()));
    }


}
