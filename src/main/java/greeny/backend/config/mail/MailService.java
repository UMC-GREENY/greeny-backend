package greeny.backend.config.mail;

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
//    private final String code = generateCode();

    @Value("${spring.mail.username}")
    private String username;

    public void sendSimpleMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = createMessage(to);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

//        return code;
    }

//    private String generateCode() {
//        StringBuilder code = new StringBuilder();
//        Random random = new Random();
//
//        for (int i = 0; i < 8; i++) {
//
//            int index = random.nextInt(3);
//
//            switch (index) {
//                case 0:
//                    code.append((char) (random.nextInt(26) + 97));
//                    break;
//                case 1:
//                    code.append((char) (random.nextInt(26) + 65));
//                    break;
//                case 2:
//                    code.append((random.nextInt(10)));
//                    break;
//            }
//        }
//
//        return code.toString();
//    }

    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("이메일 인증");

        String link = "https://www.naver.com/";  // TODO 인증된 GREENY 회원가입 url (Ex. https://greeny/sign-up/verify?token=Df2s1Gf5)
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