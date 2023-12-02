package NestNet.NestNetWebSite.service.mail;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("#{environment['spring.mail.username']}")
    private String hostAddress;                           // 메일을 보내는 호스트 이메일 주소

    /*
    아이디를 이메일로 전송
     */
    public ApiResult<?> sendEmailLoginId(String email, String loginId){

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(hostAddress);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[CBNU 네스트넷] 회원 아이디 찾기");

            StringBuilder emailBody = new StringBuilder();
            emailBody.append("<html><body style='font-size: 14px;'>");          // 폰트 크기 설정
            emailBody.append("안녕하세요 [CBNU Nestnet] 아이디 찾기 서비스 입니다.<br><br>");
            emailBody.append("<strong>로그인 ID : </strong>").append(loginId).append("<br><br>");
            emailBody.append("홈페이지로 돌아가셔서 로그인 해주세요.\n\n");
            emailBody.append("");       //홈페이지 링크 넣을 곳!!!!!!!!!!11
            emailBody.append("</body></html>");

            mimeMessageHelper.setText(emailBody.toString(), true);          //html형식으로 설정

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e){
            throw new CustomException(ErrorCode.CANNOT_SEND_EMAIL);
        }

        return ApiResult.success(email + " 에게 아이디를 전송하였습니다.");
    }

    /*
    임시 비밀번호를 이메일로 전송
     */
    public ApiResult<?> sendEmailTemporaryPassword(String email, String password){

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(hostAddress);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[CBNU 네스트넷] 임시 비밀번호 발급");

            StringBuilder emailBody = new StringBuilder();
            emailBody.append("<html><body style='font-size: 14px;'>");          // 폰트 크기 설정
            emailBody.append("안녕하세요 [CBNU Nestnet] 임시 비밀번호 발급 서비스 입니다.<br><br>");
            emailBody.append("<strong>임시 비밀번호 : </strong>").append(password).append("<br><br>");
            emailBody.append("홈페이지로 돌아가셔서 로그인 후 비밀번호를 변경해주세요.\n\n");
            emailBody.append("");       //홈페이지 링크 넣을 곳!!!!!!!!!!11
            emailBody.append("</body></html>");

            mimeMessageHelper.setText(emailBody.toString(), true);          //html형식으로 설정

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e){
            throw new CustomException(ErrorCode.CANNOT_SEND_EMAIL);
        }
        return ApiResult.success(email + " 에게 임시 비밀번호를 전송하였습니다.");
    }
}
