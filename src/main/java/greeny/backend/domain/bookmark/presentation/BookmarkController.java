package greeny.backend.domain.bookmark.presentation;

import greeny.backend.domain.bookmark.application.BookmarkService;
import greeny.backend.domain.member.application.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_TOGGLE_BOOKMARK;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@Tag(name = "Bookmark", description = "Bookmark API Document")
@Slf4j
public class BookmarkController {

    private final MemberService memberService;
    private final BookmarkService bookmarkService;

    @Operation(summary = "Toggle store or product bookmark API", description = "put type info and store or product id what you want to toggle.")
    @ResponseStatus(OK)
    @PostMapping()
    public Response toggleBookmark(String type, Long id) {
        bookmarkService.toggleStoreBookmark(type, id, memberService.getCurrentMember());
        return success(SUCCESS_TO_TOGGLE_BOOKMARK);
    }
}