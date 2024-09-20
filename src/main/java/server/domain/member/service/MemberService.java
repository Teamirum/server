package server.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.domain.member.domain.Member;
import server.domain.member.dto.MemberRequestDto;
import server.domain.member.mapper.MemberMapper;
import server.domain.member.model.Role;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberRepository;
    private final PasswordEncoder passwordEncoder;

    public String signUp(MemberRequestDto.MemberSignupRequestDto requestDto) {

        log.info("signup");
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

        return "signup";
    }
}
