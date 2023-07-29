package greeny.backend.config.mail;

import greeny.backend.domain.member.dto.sign.general.GetEmailAuthTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String token = generateToken();  // 사용자가 이메일 수신 후 '이메일 확인하기' 버튼을 누르면 이동할 url 뒤에 쿼리 파라미터로 추가되는 token

    @Value("${spring.mail.username}")
    private String username;

    // 이메일 전송
    public GetEmailAuthTokenResponseDto sendSimpleMessage(String to, String authorizationUrl) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = createMessage(to, authorizationUrl);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return new GetEmailAuthTokenResponseDto(token);
    }

    // 랜덤한 토큰 생성
    private String generateToken() {
        StringBuilder token = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {

            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    token.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    token.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    token.append((random.nextInt(10)));
                    break;
            }
        }

        return token.toString();
    }

    // 전송할 이메일 폼 구성
    private MimeMessage createMessage(String to, String authorizationUrl) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);  // 수신할 이메일 지정
        message.setSubject("이메일 인증");

        String link = authorizationUrl + "?token=" + token;  // url?token=xxx 형식
        String buttonText = "이메일 확인하기";  // link 가 있는 버튼을 눌러서 사용자를 인증된 url 로 이동시키키 위해
        String msgg="";

        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 GREENY 입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<p>이메일 인증을 완료해주세요.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' font-family:verdana';>";
        msgg+= "<div style='font-size:130%'>";
        msgg += "<a href='" + link + "' style='display:inline-block; background-color:#00FF00; color:white; padding:10px 20px; text-decoration:none;'>" + buttonText + "</a>";
        msgg += "</div></div></div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress(username,"GREENY"));  // 발송하는 이메일 지정

        return message;
    }
}