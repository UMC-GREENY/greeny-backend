package greeny.backend.domain.member.controller;

import greeny.backend.config.mail.MailService;
import greeny.backend.config.oauth.OAuthService;
import greeny.backend.domain.member.dto.sign.common.AgreementRequestDto;
import greeny.backend.domain.member.dto.sign.common.TokenRequestDto;
import greeny.backend.domain.member.dto.sign.general.FindPasswordRequestDto;
import greeny.backend.domain.member.dto.sign.general.LoginRequestDto;
import greeny.backend.domain.member.dto.sign.common.SignUpRequestDto;
import greeny.backend.domain.member.entity.Provider;
import greeny.backend.domain.member.service.AuthService;
import greeny.backend.domain.member.service.MemberService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API Document")
public class AuthController {

    private final MailService mailService;
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final MemberService memberService;

    @Operation(summary = "Authenticate email API", description = "put your email to authenticate.")
    @ResponseStatus(OK)
    @PostMapping()
    public Response sendEmail(String email) throws MessagingException, UnsupportedEncodingException {  // TODO url 파라미터로 받기
        authService.validateSignUpInfoWithGeneral(email);
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

    // 회원가입 or 소셜 로그인 동의 항목 체크 여부 요청받는 API
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

    // 카카오 로그인 API
    @Operation(summary = "Kakao sign in API", description = "put your kakao sign in info.")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/kakao")
    public Response signInWithKakao(String authorizationCode) {  // Query parameter
        return success(
                SUCCESS_TO_SIGN_IN,
                authService.signInWithSocial(oAuthService.requestToKakao(authorizationCode).getKakaoAccount().getEmail(), Provider.KAKAO));
    }

    // 네이버 로그인 API
    @Operation(summary = "Naver sign in API", description = "put your naver sign in info.")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/naver")
    public Response signInWithNaver(String authorizationCode, String state) {  // Query parameter
        return success(
                SUCCESS_TO_SIGN_IN,
                authService.signInWithSocial(oAuthService.requestToNaver(authorizationCode, state).getResponse().getEmail(), Provider.NAVER));
    }

    // 토큰 유효성 검증 API
    @Operation(summary = "Valid token API", description = "put your token info to validate")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getTokenStatusInfo(String token) {
        return success(SUCCESS_TO_VALIDATE_TOKEN, authService.getTokenStatusInfo(token));
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
        return success(SUCCESS_TO_GET_IS_AUTO, authService.getIsAutoInfo(memberService.getCurrentMember()));
    }

    @Operation(summary = "Token reissue API", description = "put your token info")
    @ResponseStatus(OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(SUCCESS_TO_REISSUE, authService.reissue(tokenRequestDto));
    }
}