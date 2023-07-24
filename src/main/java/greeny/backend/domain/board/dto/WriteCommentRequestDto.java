package greeny.backend.domain.board.dto;

import greeny.backend.domain.board.entity.Comment;
import greeny.backend.domain.board.entity.Post;
import greeny.backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WriteCommentRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 255, message = "255자 이하로 입력해주세요.")
    @Schema(description = "글 내용", defaultValue = "요즘은 비가 많이 와서 덜 더워요.")
    private String content;

    public Comment toEntity(Post post, Member writer){
        return Comment.builder()
                .post(post)
                .writer(writer)
                .content(this.content)
                .build();
    }
}
