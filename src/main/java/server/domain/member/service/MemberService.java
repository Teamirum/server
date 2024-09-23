package server.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.domain.member.domain.Member;
import server.domain.member.dto.MemberDtoConverter;
import server.domain.member.dto.MemberRequestDto;
import server.domain.member.dto.MemberResponseDto;
import server.domain.member.mapper.MemberMapper;
import server.domain.member.model.Role;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;

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
                .build();

        memberRepository.save(member);

        Member savedMember = getMemberById(member.getMemberId());

        return MemberDtoConverter.convertToMemberTaskResultResponseDto(savedMember);
    }

    private Member getMemberById(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
