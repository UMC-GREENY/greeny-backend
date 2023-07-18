package greeny.backend.domain.member.controller;

import greeny.backend.config.mail.MailService;
import greeny.backend.response.Response;
import greeny.backend.response.SuccessMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

import static greeny.backend.response.Response.*;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API Document")
public class AuthController {

    private final MailService mailService;

    @Operation(summary = "Authenticate email API", description = "put your email to authenticate.")
    @ResponseStatus(OK)
    @PostMapping("/sign-up/code")
    public Response getEmailAuthCode(String email) throws MessagingException, UnsupportedEncodingException {
        return success(SUCCESS_TO_GET_EMAIL_AUTH_CODE, mailService.sendSimpleMessage(email));
    }


}
