package greeny.backend.exception;

import greeny.backend.exception.situation.*;
import greeny.backend.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.management.relation.RoleNotFoundException;
import java.util.Objects;
import static greeny.backend.response.Response.failure;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Response illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        log.info("e = {}", e.getMessage());
        return failure(INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(CannotConvertHelperException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertHelperException e) {
        log.error("e = {}", e.getMessage());
        return failure(INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return failure(BAD_REQUEST, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(BAD_REQUEST)
    public Response tokenExpiredException() {
        return failure(BAD_REQUEST, "토큰이 만료되었습니다.");
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(BAD_REQUEST)
    public Response bindException(BindException e) {
        return failure(BAD_REQUEST, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(UNAUTHORIZED)
    public Response loginFailureException() {
        return failure(UNAUTHORIZED, "로그인에 실패하였습니다.");
    }

    @ExceptionHandler(MemberNotEqualsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public Response memberNotEqualsException() {
        return failure(UNAUTHORIZED, "유저 정보가 일치하지 않습니다.");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response memberNotFoundException() {
        return failure(NOT_FOUND, "요청한 회원을 찾을 수 없습니다.");
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response roleNotFoundException() {
        return failure(NOT_FOUND, "요청한 권한 등급을 찾을 수 없습니다.");
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(NOT_FOUND)
    public Response unsupportedImageFormatException() {
        return failure(NOT_FOUND, "이미지 형식을 지원하지 않습니다.");
    }

    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    @ResponseStatus(BAD_REQUEST)
    public Response stringIndexOutOfBoundsException() {
        return failure(BAD_REQUEST, "파일 확장자가 존재하지 않습니다.");
    }

    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        log.error("e = {}", e.getMessage());
        return failure(NOT_FOUND, "이미지 업로드 실패");
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public Response emailAlreadyExistsException(EmailAlreadyExistsException e) {
        return failure(CONFLICT, e.getMessage() + "은 중복된 이메일 입니다.");
    }

    @ExceptionHandler(EmptyFileException.class)
    @ResponseStatus(NOT_FOUND)
    public Response emptyFileException() {
        return failure(NOT_FOUND, "파일이 비어있습니다.");
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response refreshTokenNotFoundException() {
        return failure(NOT_FOUND, "요청한 토큰을 찾을 수 없습니다.");
    }

    @ExceptionHandler(MemberGeneralNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response memberGeneralNotFoundException() {
        return failure(NOT_FOUND, "요청한 일반 로그인 회원을 찾을 수 없습니다.");
    }

    @ExceptionHandler(MemberSocialNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response memberSocialNotFoundException() {
        return failure(NOT_FOUND, "요청한 소셜 로그인 회원을 찾을 수 없습니다.");
    }

    @ExceptionHandler(MemberProfileNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response memberProfileNotFoundException() {
        return failure(NOT_FOUND, "요청한 회원 프로필 정보를 찾을 수 없습니다.");
    }

    @ExceptionHandler(MemberAgreementNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response memberAgreementNotFoundException() {
        return failure(NOT_FOUND, "요청한 회원의 동의 항목을 찾을 수 없습니다.");
    }

    @ExceptionHandler(EmptySocialTokenInfoException.class)
    @ResponseStatus(BAD_REQUEST)
    public Response emptySocialTokenInfoException() {
        return failure(BAD_REQUEST, "소셜에서 제공한 토큰 정보가 비어있습니다.");
    }

    @ExceptionHandler(StoreNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response storeNotFoundException() {
        return failure(NOT_FOUND, "요청한 스토어를 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response productNotFoundException() {
        return failure(NOT_FOUND, "요청한 제품을 찾을 수 없습니다.");
    }

    @ExceptionHandler(TypeDoesntExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    public Response typeDoesntExistsException() {
        return failure(BAD_REQUEST, "타입이 존재하지 않습니다.");
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response postNotFoundException() {
        return failure(NOT_FOUND, "요청한 게시글을 찾을 수 없습니다.");
    }

    @ExceptionHandler(SelfLikeNotAllowedException.class)
    @ResponseStatus(FORBIDDEN)
    public Response selfLikeNotAllowedException() {
        return failure(NOT_FOUND, "게시글 작성자는 자기의 게시글에 좋아요를 누를 수 없습니다.");
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Response commentNotFoundException() {
        return failure(NOT_FOUND, "요청한 댓글을 찾을 수 없습니다.");
    }

    @ExceptionHandler(ReviewNotFound.class)
    @ResponseStatus(NOT_FOUND)
    public Response reviewNotFoundException() { return failure(NOT_FOUND,"요청한 리뷰를 찾을 수 없습니다."); }
}