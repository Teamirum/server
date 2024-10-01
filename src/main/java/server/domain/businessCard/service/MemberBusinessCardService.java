package server.domain.businessCard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.businessCard.domain.BusinessCard;
import server.domain.businessCard.domain.MemberBusinessCard;
import server.domain.businessCard.dto.BusinessCardRequestDto;
import server.domain.businessCard.dto.MemberBusinessCardRequestDto;
import server.domain.businessCard.dto.MemberBusinessCardResponseDto;
import server.domain.businessCard.repository.BusinessCardRepository;
import server.domain.businessCard.repository.MemberBusinessCardRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberBusinessCardService {

    private final MemberBusinessCardRepository memberBusinessCardRepository;

    // MemberBusinessCard 저장 메소드
    public void save(MemberBusinessCard memberBusinessCard) {
        memberBusinessCardRepository.save(memberBusinessCard);
    }

    public boolean existsMemberBusinessCardByMemberIdx(Long memberIdx) {
        return memberBusinessCardRepository.existsMemberBusinessCardByMemberIdx(memberIdx);
    }
}
