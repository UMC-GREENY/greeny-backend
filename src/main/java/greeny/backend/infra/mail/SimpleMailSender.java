package greeny.backend.infra.mail;

import greeny.backend.domain.member.presentation.dto.sign.general.GetEmailAuthInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SimpleMailSender {

    private final JavaMailSender javaMailSender;
    private final String token = generateToken();

    @Value("${spring.mail.username}")
    private String username;

    public GetEmailAuthInfoResponseDto sendSimpleMessage(String to, String authorizationUrl) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = createMessage(to, authorizationUrl);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return GetEmailAuthInfoResponseDto.builder()
                .email(to)
                .token(token)
                .build();
    }

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

    private MimeMessage createMessage(String to, String authorizationUrl) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("이메일 인증");

        String link = authorizationUrl + "?token=" + token;
        String buttonText = "이메일 확인하기";
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
        message.setFrom(new InternetAddress(username,"GREENY"));

        return message;
    }
}