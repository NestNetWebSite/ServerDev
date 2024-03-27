package NestNet.NestNetWebSite.service.slack;

import NestNet.NestNetWebSite.exception.CustomException;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
@Slf4j
public class SlackService {

    @Value("${logging.webhook.slack-url}")
    private String slackWebHookUrl;

    private final Slack slackClient = Slack.getInstance();

    // error : 즉시 전송
    public void sendSlackErrorLog(CustomException e, HttpServletRequest request) {
        try {
            slackClient.send(slackWebHookUrl, payload(p -> p
                    .text("\uD83D\uDEA8" +
                            " 서버에 에러가 감지되었습니다. 즉시 확인이 필요합니다. " +
                            "\uD83D\uDEA8")
                    .attachments(
                            List.of(generateSlackErrorAttachment(e, request))
                    )
            ));
        } catch (IOException slackError) {
            // 실제 에러 로그가 아닌 slack과의 통신 장애이기 때문에 error가 아닌 debug로 log
            log.debug("Slack 통신 과정에 예외 발생");
        }
    }

    // 메세지 내에 첨부될 정보를 추가, 생성
    private Attachment generateSlackErrorAttachment(CustomException e, HttpServletRequest request) {

        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
        String xffHeader = request.getHeader("X-FORWARDED-FOR");        // 프록시/로드밸런서를 통해 클라이언트의 원 ip 주소를 가져옴

        StringBuilder requestUrlBuilder = new StringBuilder()
                .append(request.getMethod()).append(" ").append(request.getRequestURL());

        return Attachment.builder()
                .color("ff0000")
                .title(requestTime)
                .fields(List.of(
                                generateSlackField("Request IP", xffHeader == null ? request.getRemoteAddr() : xffHeader),
                                generateSlackField("Request URL", requestUrlBuilder.toString()),
                                generateSlackField("Error Code", e.getErrorCode().getHttpStatus().toString()),
                                generateSlackField("Error Message", e.getErrorCode().getMsg())
                        )
                )
                .build();
    }

    // 슬랙의 단락별 제목과 내용을 구성하는 객체 생성
    private Field generateSlackField(String title, String value) {

        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
