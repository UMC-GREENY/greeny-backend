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

    public static final String SUCCESS_TO_GET_MEMBER_INFO = "회원의 정보를 가져오는데 성공했습니다.";
    public static final String SUCCESS_TO_EDIT_MEMBER_PASSWORD = "회원 비밀번호를 변경하는데 성공했습니다.";
    public static final String SUCCESS_TO_DELETE_MEMBER = "회원을 삭제하는데 성공했습니다.";
    public static final String SUCCESS_TO_CANCEL_BOOKMARK = "회원의 찜한 목록에서 삭제하는데 성공했습니다.";

    public static final String SUCCESS_TO_GET_SIMPLE_STORE_INFOS = "스토어 목록을 불러오는데 성공했습니다.";
    public static final String SUCCESS_TO_GET_STORE_INFO = "스토어 상세 정보를 불러오는데 성공했습니다.";

    public static final String SUCCESS_TO_GET_SIMPLE_PRODUCT_INFOS = "제품 목록을 불러오는데 성공했습니다.";
    public static final String SUCCESS_TO_GET_PRODUCT_INFO = "제품 상세 정보를 불러오는데 성공했습니다.";

    public static final String SUCCESS_TO_TOGGLE_BOOKMARK = "찜하기 or 취소에 성공했습니다.";

    public static final String SUCCESS_TO_WRITE_POST = "게시글을 작성하는데 성공하였습니다.";
    public static final String SUCCESS_TO_GET_POST_LIST = "게시글 목록을 불러오는데 성공하였습니다.";
    public static final String SUCCESS_TO_SEARCH_POST_LIST = "게시글 목록을 검색하는데 성공하였습니다.";
    public static final String SUCCESS_TO_GET_POST = "게시글 상세정보를 불러오는데 성공하였습니다.";
    public static final String SUCCESS_TO_DELETE_POST = "게시글을 삭제하는데 성공하였습니다.";
    public static final String SUCCESS_TO_EDIT_POST = "게시글을 수정하는데 성공하였습니다.";

    public static final String SUCCESS_TO_WRITE_COMMENT = "댓글을 작성하는데 성공하였습니다.";
    public static final String SUCCESS_TO_GET_COMMENT_LIST = "댓글 목록을 불러오는데 성공하였습니다.";
    public static final String SUCCESS_TO_DELETE_COMMENT = "댓글을 삭제하는데 성공하였습니다.";
    public static final String SUCCESS_TO_EDIT_COMMENT = "댓글을 수정하는데 성공하였습니다.";
}