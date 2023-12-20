package NestNet.NestNetWebSite.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 BAD REQUEST
    ID_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "아이디 / 비밀번호가 일치하지 않습니다."),
    CODE_NOT_CORRECT(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다. \n하지만, 이메일을 정상적으로 받았으면 인증 코드에 nestnet을 입력하세요"),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 정보가 없습니다. 다시 로그인 하세요"),

    // 403 FORBIDDEN
    MEMBER_NOT_PERMISSION_YET(HttpStatus.FORBIDDEN, "아직 승인되지 않은 회원입니다. 관리자에게 문의하세요"),
    MEMBER_NO_PERMISSION(HttpStatus.FORBIDDEN, "해당 회원에 권한이 없습니다. 본인만 조회 가능합니다."),

    // 404 NOT FOUND
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파일이 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원이 없습니다."),
    MEMBER_LOGIN_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 로그인 아이디에 해당하는 회원이 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
    THUMBNAIL_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사진 게시물의 썸네일 파일이 존재하지 않습니다."),
    MEMBER_SIGNUP_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "회원가입 요청을 찾을 수 없습니다."),

    // 409 CONFLICT
    ALREADY_ATTENDED(HttpStatus.CONFLICT, "이미 출석하셨습니다."),
    ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다."),

    // 500
    CANNOT_SAVE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다. 관리자에게 문의하세요."),
    CANNOT_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 이메일 전송을 실패하였습니다. 관리자에게 문의하세요"),;


    private final HttpStatus httpStatus;
    private final String msg;
}
