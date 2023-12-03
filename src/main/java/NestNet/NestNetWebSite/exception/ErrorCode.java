package NestNet.NestNetWebSite.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 BAD REQUEST
    ALREADY_ATTENDED(HttpStatus.BAD_REQUEST, "이미 출석하셨습니다."),

    // 404 NOT FOUND
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파일이 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원이 없습니다."),
    MEMBER_LOGIN_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 로그인 아이디에 해당하는 회원이 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
    THUMBNAIL_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사진 게시물의 썸네일 파일이 존재하지 않습니다."),

    // 500
    CANNOT_SAVE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다. 관리자에게 문의하세요."),
    CANNOT_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 이메일 전송을 실패하였습니다. 관리자에게 문의하세요"),


    private final HttpStatus httpStatus;
    private final String msg;
}
