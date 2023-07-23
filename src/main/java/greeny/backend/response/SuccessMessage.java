package greeny.backend.response;

import lombok.Getter;

@Getter
public class SuccessMessage {
    public static final String SUCCESS = "요청에 성공했습니다";
    public static final String SUCCESS_TO_SEND_EMAIL = "이메일을 전송하는데 성공했습니다.";
    public static final String SUCCESS_TO_SIGN_UP = "회원가입에 성공했습니다.";
    public static final String SUCCESS_TO_SIGN_UP_AGREEMENT = "회원가입 동의 항목 선택에 성공했습니다.";
    public static final String SUCCESS_TO_SIGN_IN = "로그인에 성공했습니다.";
    public static final String SUCCESS_TO_VALIDATE_TOKEN = "토큰 유효성 검증에 성공했습니다.";
    public static final String SUCCESS_TO_GET_IS_AUTO = "자동 로그인 여부 정보를 가져오는데 성공했습니다.";
    public static final String SUCCESS_TO_FIND_PASSWORD = "비밀번호를 찾는데 성공했습니다.";
    public static final String SUCCESS_TO_REISSUE = "토큰 재발급에 성공했습니다.";

    public static final String SUCCESS_TO_GET_CURRENT_MEMBER_INFO = "현재 회원의 정보를 가져오는데 성공했습니다.";
    public static final String SUCCESS_TO_DELETE_MEMBER = "회원을 삭제하는데 성공했습니다.";
}
