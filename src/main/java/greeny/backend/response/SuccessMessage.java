package greeny.backend.response;

import lombok.Getter;

@Getter
public class SuccessMessage {
    public static final String SUCCESS = "요청에 성공했습니다";
    public static final String SUCCESS_TO_SEND_EMAIL = "이메일을 전송하는데 성공했습니다.";
    public static final String SUCCESS_TO_SIGN_UP = "회원가입에 성공했습니다.";
    public static final String SUCCESS_TO_SIGN_IN = "로그인에 성공했습니다.";
    public static final String SUCCESS_TO_GET_IS_AUTO = "자동 로그인 여부 정보를 가져오는데 성공했습니다.";
    public static final String SUCCESS_TO_FIND_PASSWORD = "비밀번호를 찾는데 성공했습니다.";
    public static final String SUCCESS_TO_REISSUE = "토큰 재발급에 성공했습니다.";

    public static final String SUCCESS_TO_GET_CURRENT_MEMBER_INFO = "현재 회원의 정보를 가져오는데 성공했습니다.";
    public static final String SUCCESS_TO_DELETE_MEMBER = "회원을 삭제하는데 성공했습니다.";

    public static final String SUCCESS_TO_CREATE_POST = "게시글을 작성하는데 성공하였습니다.";
    public static final String SUCCESS_TO_SEARCH_POST_LIST = "게시글 목록을 검색하는데 성공하였습니다.";
    public static final String SUCCESS_TO_GET_POST = "게시글 상세정보를 불러오는데 성공하였습니다.";
    public static final String SUCCESS_TO_DELETE_POST = "게시글을 삭제하는데 성공하였습니다.";
    public static final String SUCCESS_TO_UPDATE_POST = "게시글을 수정하는데 성공하였습니다.";

    public static final String SUCCESS_TO_CREATE_COMMENT = "댓글을 작성하는데 성공하였습니다.";
    public static final String SUCCESS_TO_GET_COMMENT_LIST = "댓글 목록을 불러오는데 성공하였습니다.";
    public static final String SUCCESS_TO_DELETE_COMMENT = "댓글을 삭제하는데 성공하였습니다.";
    public static final String SUCCESS_TO_UPDATE_COMMENT = "댓글을 수정하는데 성공하였습니다.";

}