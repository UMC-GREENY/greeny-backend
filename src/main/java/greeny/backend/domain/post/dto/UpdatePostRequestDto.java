package greeny.backend.domain.post.dto;

import greeny.backend.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    @Schema(description = "제목", defaultValue = "게시글 제목 수정할게요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    @Schema(description = "내용", defaultValue = "게시글 내용 수정할게요.")
    private String content;

}