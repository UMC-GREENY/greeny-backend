package greeny.backend.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 250, message = "255자 이하로 입력해주세요.")
    @Schema(description = "제목", defaultValue = "게시글 제목 수정할게요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 500, message = "500자 이하로 입력해주세요.")
    @Schema(description = "내용", defaultValue = "게시글 내용 수정할게요.")
    private String content;

}