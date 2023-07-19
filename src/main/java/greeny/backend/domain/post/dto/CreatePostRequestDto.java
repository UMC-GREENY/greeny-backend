package greeny.backend.domain.post.dto;

import greeny.backend.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    @Schema(description = "글 제목", defaultValue = "요즘 날씨 너무 덥네요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    @Schema(description = "글 내용", defaultValue = "빨리 가을이 왔으면 좋겠어요.")
    private String content;

    public Post toEntity(){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .hits(0)
                .build();
    }
}
