package greeny.backend.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
@JsonInclude(NON_NULL)
@Schema(description = "This is response message from server")
public class Response {
    private Boolean isSuccess;
    private int code;
    private String message;
    private Object data;

    public static Response success(String message) {
        return new Response(true, OK.value(), message, null);
    }

    public static Response success(String message, Object data) {
        return new Response(true, OK.value(), message, data);
    }

    public static Response failure(HttpStatus status, String message) {
        return new Response(false, status.value(), message, null);
    }
}