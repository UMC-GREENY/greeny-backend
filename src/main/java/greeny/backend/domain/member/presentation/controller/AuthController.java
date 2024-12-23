package greeny.backend.domain.member.presentation.controller;

import greeny.backend.infra.mail.SimpleMailSender;
import greeny.backend.infra.oauth.OAuthService;
import greeny.backend.domain.member.presentation.dto.sign.common.AgreementRequestDto;
import greeny.backend.domain.member.presentation.dto.sign.common.TokenRequestDto;
import greeny.backend.domain.member.presentation.dto.sign.general.AuthEmailRequestDto;
import greeny.backend.domain.member.presentation.dto.sign.general.FindPasswordRequestDto;
import greeny.backend.domain.member.presentation.dto.sign.general.LoginRequestDto;
import greeny.backend.domain.member.presentation.dto.sign.general.SignUpRequestDto;
import greeny.backend.domain.member.entity.Provider;
import greeny.backend.domain.member.application.AuthService;
import greeny.backend.domain.member.application.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import static greeny.backend.response.Response.*;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API Document")
@Slf4j
public class AuthController {

    private final SimpleMailSender simpleMailSender;
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final MemberService memberService;

    @Operation(summary = "Authenticate email API", description = "put your email to authenticate.")
    @ResponseStatus(OK)
    @PostMapping()
    public Response sendEmail(@Valid @RequestBody AuthEmailRequestDto authEmailRequestDto) throws MessagingException, UnsupportedEncodingException {
        String email = authEmailRequestDto.getEmail();
        authService.validateSignUpInfoWithGeneral(email);
        return success(SUCCESS_TO_SEND_EMAIL, simpleMailSender.sendSimpleMessage(email, authEmailRequestDto.getAuthorizationUrl()));
    }

    @Operation(summary = "Valid token API", description = "put your token info to what you want to validate.")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getTokenStatusInfo(@RequestHeader("Authorization") String bearerToken) {
        return success(SUCCESS_TO_VALIDATE_TOKEN, authService.getTokenStatusInfo(bearerToken));
    }

    @Operation(summary = "Sign up API", description = "put your sign up info.")
    @ResponseStatus(CREATED)
    @PostMapping("/sign-up")
    public Response signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return success(SUCCESS_TO_SIGN_UP);
    }

    @Operation(summary = "Social sign up agreement API", description = "put your social sign up agreement info.")
    @ResponseStatus(CREATED)
    @PostMapping("/sign-up/agreement")
    public Response agreementInSignUp(@Valid @RequestBody AgreementRequestDto agreementRequestDto) {
        return success(SUCCESS_TO_SIGN_UP_AGREEMENT, authService.agreementInSignUp(agreementRequestDto));
    }

    @Operation(summary = "General sign in API", description = "put your sign in info.")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/general")
    public Response signInWithGeneral(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return success(SUCCESS_TO_SIGN_IN, authService.signInWithGeneral(loginRequestDto));
    }

    @Operation(summary = "Kakao sign in API", description = "put your kakao sign in info.")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/kakao")
    public Response signInWithKakao(String authorizationCode) {
        return success(
                SUCCESS_TO_SIGN_IN,
                authService.signInWithSocial(oAuthService.requestToKakao(authorizationCode).getKakaoAccount().getEmail(), Provider.KAKAO));
    }

    @Operation(summary = "Naver sign in API", description = "put your naver sign in info.")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/naver")
    public Response signInWithNaver(String authorizationCode, String state) {  // Query parameter
        return success(
                SUCCESS_TO_SIGN_IN,
                authService.signInWithSocial(oAuthService.requestToNaver(authorizationCode, state).getResponse().getEmail(), Provider.NAVER));
    }

    @Operation(summary = "Find password API", description = "put your email.")
    @ResponseStatus(OK)
    @PatchMapping("/password")
    public Response findPassword(@Valid @RequestBody FindPasswordRequestDto findPasswordRequestDto) {
        authService.findPassword(findPasswordRequestDto);
        return success(SUCCESS_TO_FIND_PASSWORD);
    }

    @Operation(summary = "Auto sign in API", description = "please auto sign in.")
    @ResponseStatus(OK)
    @GetMapping("/auto/sign-in")
    public Response getIsAutoInfo() {
        return success(SUCCESS_TO_GET_IS_AUTO, authService.getIsAutoInfo(memberService.getCurrentMember().getId()));
    }

    @Operation(summary = "Token reissue API", description = "put your token info")
    @ResponseStatus(OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(SUCCESS_TO_REISSUE, authService.reissue(tokenRequestDto));
    }
}