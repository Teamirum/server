package server.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.domain.member.domain.Member;
import server.domain.member.dto.MemberDtoConverter;
import server.domain.member.dto.MemberRequestDto;
import server.domain.member.dto.MemberResponseDto;
import server.domain.member.model.Role;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.auth.oauth2.model.SocialType;
import server.global.auth.security.domain.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto.MemberTaskResultResponseDto signUp(MemberRequestDto.MemberSignupRequestDto requestDto) {
        if (memberRepository.existsByMemberId(requestDto.getMemberId()) || memberRepository.existsByPhoneNum(requestDto.getPhoneNum())) {
            throw new ErrorHandler(ErrorStatus.MEMBER_ALREADY_EXIST);
        }
        Member member = Member.builder()
                .memberId(requestDto.getMemberId())
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .phoneNum(requestDto.getPhoneNum())
                .role(Role.ROLE_MEMBER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .socialType(SocialType.LOCAL)
                .build();

        memberRepository.save(member);

        Member savedMember = getMemberByMemberId(member.getMemberId());
        log.info(savedMember.getSocialType().name());
        log.info(savedMember.getSocialType().getSocialName());
        return MemberDtoConverter.convertToMemberTaskResultResponseDto(savedMember);
    }

    public Member createDefaultOAuth2Member(CustomUserDetails oAuth2User) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Member member = Member.builder()
                .memberId(oAuth2User.getMemberId())
                .password(oAuth2User.getPassword())
                .name(oAuth2User.getMemberName())
                .email(oAuth2User.getEmail())
                .role(Role.ROLE_GUEST)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .socialType(oAuth2User.getSocialType())
                .build();

        log.info("신규 소셜 회원입니다. 등록을 진행합니다. memberId = {}, email = {}, socialType = {}, name = {}", member.getMemberId(), member.getEmail(), member.getSocialType().getSocialName(), member.getName());
        memberRepository.save(member);
        return member;
    }


    private Member getMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public Long findByMemberId(String loginMemberId) {
        return memberRepository.getIdxByMemberId(loginMemberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
