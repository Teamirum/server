package server.domain.businessCard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.businessCard.domain.BusinessCard;
import server.domain.businessCard.dto.BusinessCardDtoConverter;
import server.domain.businessCard.dto.BusinessCardRequestDto;
import server.domain.businessCard.dto.BusinessCardResponseDto;
import server.domain.businessCard.repository.BusinessCardRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessCardService {

    private final BusinessCardRepository businessCardRepository;
    private  final MemberRepository memberRepository;

    public BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto upload(BusinessCardRequestDto.UploadBusinessCardRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (existsBusinessCardByMemberIdx(memberIdx)) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_CARD_DUPLICATE);
        }

        BusinessCard businessCard = BusinessCard.builder()
                .memberIdx(memberIdx)
                .name(requestDto.getName())
                .position(requestDto.getPosition())
                .company(requestDto.getCompany())
                .email(requestDto.getEmail())
                .phoneNum(requestDto.getPhoneNum())
                .address(requestDto.getAddress())
                .build();

        businessCardRepository.save(businessCard);

        BusinessCard savedBusinessCard = businessCardRepository.findByMemberIdx(businessCard.getMemberIdx())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BUSINESS_CARD_SAVE_FAIL));

        return BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedBusinessCard.getIdx())
                .build();
    }

    public boolean existsBusinessCardByMemberIdx(Long memberIdx) {
        return businessCardRepository.existsBusinessCardByMemberIdx(memberIdx);
    }


    public BusinessCardResponseDto.BusinessCardListResponseDto getBusinessCardList(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<BusinessCard> businessCardList = businessCardRepository.findAllBusinessCardByMemberIdx(memberIdx);
        return BusinessCardDtoConverter.convertToBusinessCardListResponseDto(businessCardList);
    }

    public BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto delete(Long idx, String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (businessCardRepository.findByBusinessCardIdx(idx).isEmpty()) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND);
        }
        businessCardRepository.delete(idx);
        return BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .build();
    }

//    public BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto updateBusinessCard(BusinessCardRequestDto.UpdateBusinessCardRequestDto requestDto) {
//       businessCardRepository.updateBusinessCard(requestDto.getIdx(), requestDto.getName(), requestDto.getPhoneNum(), requestDto.getEmail(), requestDto.getPosition(), requestDto.getPart(), requestDto.getCompany(), requestDto.getAddress(), requestDto.getFaxNumber(), requestDto.getMemo());
//        return BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto.builder()
//                .isSuccess(true)
//                .idx(requestDto.getIdx())
//                .build();
//    }

}
