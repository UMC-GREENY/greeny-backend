package greeny.backend.domain.member.controller;

import greeny.backend.config.mail.MailService;
import greeny.backend.domain.member.dto.sign.LoginRequestDto;
import greeny.backend.domain.member.dto.sign.SignUpRequestDto;
import greeny.backend.domain.member.service.AuthService;
import greeny.backend.response.Response;
import greeny.backend.response.SuccessMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
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
    private final AuthService authService;

    @Operation(summary = "Authenticate email API", description = "put your email to authenticate.")
    @ResponseStatus(OK)
    @PostMapping()
    public Response sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        authService.validateSignUpInfo(email);
        mailService.sendSimpleMessage(email);
        return success(SUCCESS_TO_SEND_EMAIL);
    }

    @Operation(summary = "Sign up API", description = "put your sign up info.")
    @ResponseStatus(CREATED)
    @PostMapping("/sign-up")
    public Response signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return success(SUCCESS_TO_SIGN_UP);
    }

    @Operation(summary = "General sign in API", description = "put your sign in info.")
    @PostMapping("/sign-in/general")
    @ResponseStatus(OK)
    public Response signInWithGeneral(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return success(SUCCESS_TO_SIGN_IN, authService.signInWithGeneral(loginRequestDto));
    }


}
