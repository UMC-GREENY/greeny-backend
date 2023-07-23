package greeny.backend.domain.member.controller;


import greeny.backend.domain.member.dto.member.EditMemberInfoRequestDto;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_EDIT_MEMBER_PASSWORD;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/members/password")
@Tag(name = "Member", description = "Member API Document")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Edit member API", description = "put info what you want to change")
    @ResponseStatus(OK)
    @PatchMapping("")
    public Response editMemberInfo(@Valid @RequestBody EditMemberInfoRequestDto editMemberRequestDto) {
        //입력 받은 dto를 edit메서드로 db에 변경된 비밀번호를 저장합니다.
        memberService.editMemberInfo(editMemberRequestDto);
        //변경이 완료 되었다면 메세지를 반환합니다.
        return success(SUCCESS_TO_EDIT_MEMBER_PASSWORD);
    }
}
