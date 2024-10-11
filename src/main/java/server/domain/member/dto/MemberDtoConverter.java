package server.domain.member.dto;

import server.domain.member.domain.Member;

public class MemberDtoConverter {


    public static MemberResponseDto.MemberTaskResultResponseDto convertToMemberTaskResultResponseDto(Member member) {
        return MemberResponseDto.MemberTaskResultResponseDto.builder()
                .idx(member.getIdx())
                .memberId(member.getMemberId())
                .isSuccess(true)
                .build();
    }

    public static MemberResponseDto.MemberInfoResponseDto convertToMemberInfoResponseDto(Member member) {
        return MemberResponseDto.MemberInfoResponseDto.builder()
                .idx(member.getIdx())
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt().toString())
                .build();
    }
}
