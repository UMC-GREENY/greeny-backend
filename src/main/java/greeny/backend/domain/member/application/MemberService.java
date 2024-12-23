package greeny.backend.domain.member.application;

import greeny.backend.domain.bookmark.entity.ProductBookmarkRepository;
import greeny.backend.domain.bookmark.entity.StoreBookmarkRepository;
import greeny.backend.domain.member.presentation.dto.CancelBookmarkRequestDto;
import greeny.backend.domain.member.presentation.dto.EditMemberInfoRequestDto;
import greeny.backend.domain.member.presentation.dto.GetMemberInfoResponseDto;
import greeny.backend.domain.member.entity.*;
import greeny.backend.exception.situation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

import static greeny.backend.domain.Target.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberSocialRepository memberSocialRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberAgreementRepository memberAgreementRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StoreBookmarkRepository storeBookmarkRepository;
    private final ProductBookmarkRepository productBookmarkRepository;
    private final AuthService authService;

    public Member getCurrentMember() {
        return memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(MemberNotFoundException::new);
    }

    public GetMemberInfoResponseDto getMemberInfo() {

        Member currentMember = getCurrentMember();
        Long currentMemberId = currentMember.getId();

        if(memberGeneralRepository.existsByMemberId(currentMemberId)) {
            MemberProfile currentMemberInfo = getMemberProfile(currentMemberId);
            return GetMemberInfoResponseDto.toGeneralMemberDto(
                    currentMember.getEmail(),
                    currentMemberInfo.getName(),
                    currentMemberInfo.getPhone(),
                    currentMemberInfo.getBirth()
            );
        }

        return GetMemberInfoResponseDto.toSocialMemberDto(currentMember.getEmail(), getMemberSocial(currentMemberId).getProvider().getName());
    }

    public void deleteMember() {

        Member currentMember = getCurrentMember();
        String key = currentMember.getEmail();

        if(refreshTokenRepository.existsById(key)) {
            refreshTokenRepository.deleteById(key);
        }

        checkAndDeleteGeneralOrSocialMember(currentMember, currentMember.getId());
    }

    @Transactional
    public void editMemberInfo(EditMemberInfoRequestDto editMemberRequestDto) {

        MemberGeneral currentGeneralMember = authService.getMemberGeneral(getCurrentMember().getId());

        if(!passwordEncoder.matches(editMemberRequestDto.getPasswordToCheck(), currentGeneralMember.getPassword())) {
            throw new MemberNotEqualsException();
        }

        currentGeneralMember.changePassword(passwordEncoder.encode(editMemberRequestDto.getPasswordToChange()));
    }

    @Transactional
    public void cancelBookmark(String type, CancelBookmarkRequestDto cancelBookmarkRequestDto) {
        checkAndCancelBookmark(type, cancelBookmarkRequestDto.getIdsToDelete());
    }

    private MemberProfile getMemberProfile(Long memberId) {
        return memberProfileRepository.findByMemberId(memberId)
                .orElseThrow(MemberProfileNotFoundException::new);
    }

    private MemberAgreement getMemberAgreement(Long memberId) {
        return memberAgreementRepository.findByMemberId(memberId)
                .orElseThrow(MemberAgreementNotFoundException::new);
    }

    private MemberSocial getMemberSocial(Long memberId) {
        return memberSocialRepository.findByMemberId(memberId)
                .orElseThrow(MemberSocialNotFoundException::new);
    }

    private void checkAndDeleteGeneralOrSocialMember(Member currentMember, Long currentMemberId) {
        if(memberGeneralRepository.existsByMemberId(currentMemberId)) {
            memberGeneralRepository.delete(authService.getMemberGeneral(currentMemberId));
            memberProfileRepository.delete(getMemberProfile(currentMemberId));
        }
        else {
            memberSocialRepository.delete(getMemberSocial(currentMemberId));
        }
        memberAgreementRepository.delete(getMemberAgreement(currentMemberId));
        memberRepository.delete(currentMember);
    }

    private void checkAndCancelBookmark(String type, List<Long> idsToDelete) {
        if(type.equals(STORE.toString())) {
            storeBookmarkRepository.deleteStoreBookmarksByIds(idsToDelete);
        }
        else if(type.equals(PRODUCT.toString())) {
            productBookmarkRepository.deleteProductBookmarksByIds(idsToDelete);
        }
        else {
            throw new TypeDoesntExistsException();
        }
    }
}