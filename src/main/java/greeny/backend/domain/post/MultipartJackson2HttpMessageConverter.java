package greeny.backend.domain.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    // request header의 content-type이 multipart/form-data이고, request body에 json으로 받아야 하는 dto가 있을 때,
    // swagger에서 get post api/update post api 테스트 시 dto를 json으로 받아오지 못하고 octet-stream으로 받아오는 문제가 발생

    // 이 클래스는 request body의 dto가 octet-stream으로 넘어오면 json으로 바꿔주기 위함

    /**
     * Converter for support http request with header Content-Type: multipart/form-data
     */
    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        // Convert http request with APPLICATION_OCTET_STREAM to APPLICATION_JSON
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    // Prohibit writing, allow only reading
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }
}