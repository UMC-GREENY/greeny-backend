package greeny.backend.domain.community.presentation.dto;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.community.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WritePostRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 255, message = "255자 이하로 입력해주세요.")
    @Schema(description = "글 제목", defaultValue = "요즘 날씨 너무 덥네요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 500, message = "500자 이하로 입력해주세요.")
    @Schema(description = "글 내용", defaultValue = "빨리 가을이 왔으면 좋겠어요.")
    private String content;

    public Post toEntity(Member writer, Boolean hasPostFile){
        return Post.builder()
                .writer(writer)
                .title(this.title)
                .content(this.content)
                .hits(0)
                .hasPostFile(hasPostFile)
                .build();
    }
}