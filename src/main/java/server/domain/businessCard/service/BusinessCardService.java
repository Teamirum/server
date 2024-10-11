package server.domain.businessCard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.domain.businessCard.domain.BusinessCard;
import server.domain.businessCard.domain.MemberBusinessCard;
import server.domain.businessCard.dto.*;
import server.domain.businessCard.repository.BusinessCardRepository;
import server.domain.businessCard.repository.MemberBusinessCardRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.util.List;

import static server.domain.businessCard.domain.MemberBusinessCard.Status.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessCardService {

    private final BusinessCardRepository businessCardRepository;
    private final MemberRepository memberRepository;
    private final MemberBusinessCardRepository memberBusinessCardRepository;

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

    public BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto delete(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (businessCardRepository.findByMemberIdx(memberIdx).isEmpty()) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND);
        }

        BusinessCard businessCard = businessCardRepository.findByMemberIdx(memberIdx)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND));


        businessCardRepository.delete(memberIdx);

        return BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(businessCard.getIdx())
                .build();
    }

    public BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto update(BusinessCardRequestDto.UpdateBusinessCardRequestDto requestDto, String loginMemberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(loginMemberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        BusinessCard businessCard = businessCardRepository.findByBusinessCardIdx(requestDto.getIdx())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND));

        if (!businessCard.getMemberIdx().equals(memberIdx)) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_CARD_UPDATE_FAIL);
        }

        businessCard.setName(requestDto.getName());
        businessCard.setPosition(requestDto.getPosition());
        businessCard.setCompany(requestDto.getCompany());
        businessCard.setEmail(requestDto.getEmail());
        businessCard.setPhoneNum(requestDto.getPhoneNum());
        businessCard.setAddress(requestDto.getAddress());
        businessCard.setPart(requestDto.getPart());

        businessCardRepository.updateBusinessCard(businessCard);

        return BusinessCardResponseDto.BusinessCardTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(businessCard.getIdx())
                .build();
    }


    // 친구 QR 등록시 사용
    public BusinessCard getBusinessCard(Long idx, String loginMemberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(loginMemberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        BusinessCard businessCard = businessCardRepository.findByBusinessCardIdx(idx)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND));

        return businessCard;
    }

    public BusinessCard findMemberIdx(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return businessCardRepository.findByMemberIdx(memberIdx)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND));
    }


    public void uploadMemberBusinessCard(BusinessCard businessCard) {


        memberBusinessCardRepository.save(MemberBusinessCard.builder()
                .memberIdx(businessCard.getMemberIdx())
                .businessCardIdx(businessCard.getIdx())
                .status(OWNER)
                .memo("")
                .build());
    }

    public void addFriendBusinessCard(Long businessCardIdx, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (businessCardRepository.existsByMemberIdxAndBusinessCardIdx(businessCardIdx, memberIdx)) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_CARD_DUPLICATE);
        }

        memberBusinessCardRepository.save(MemberBusinessCard.builder()
                .memberIdx(memberIdx)
                .businessCardIdx(businessCardIdx)
                .status(NOT_OWNER)
                .memo("")
                .build());
    }

    public BusinessCardResponseDto.BusinessCardListResponseDto getAllFriendBusinessCards(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if(!memberBusinessCardRepository.existsByMemberIdxAndStatus(memberIdx)) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_FRIEND_CARD_NOT_FOUND);
        }

        List<BusinessCard> businessCardList = businessCardRepository.findAllFriendBusinessCards(memberIdx);
        return BusinessCardDtoConverter.convertToBusinessCardListResponseDto(businessCardList);
    }

    public MemberBusinessCardResponseDto.MemberBusinessCardTaskSuccessResponseDto deleteFriendBusinessCard(Long businessCardIdx, String memberId) {
        log.info("서비스에서 전달된 BusinessCardIdx: {}", businessCardIdx);
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (memberBusinessCardRepository.findByMemberIdxAndBusinessCardIdx(memberIdx, businessCardIdx).isEmpty()) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND);
        }



        memberBusinessCardRepository.delete(memberIdx, businessCardIdx);
        return MemberBusinessCardResponseDto.MemberBusinessCardTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(businessCardIdx)
                .build();
    }

    // 특정 businessCardIdx와 memberId를 통해 명함 조회
    public BusinessCardResponseDto.BusinessCardListResponseDto getFriendBusinessCard(Long businessCardIdx, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        BusinessCard businessCard = businessCardRepository.findByBusinessCardIdx(businessCardIdx)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND));

        if (!memberBusinessCardRepository.existsFriendBusinessCardIdx(businessCardIdx, memberIdx)) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_FRIEND_CARD_NOT_FOUND);
        }

        return BusinessCardDtoConverter.convertToBusinessCardListResponseDto(List.of(businessCard));
    }

    public MemberBusinessCardResponseDto.MemberBusinessCardTaskSuccessResponseDto updateFriendBusinessCard(Long businessCardIdx, MemberBusinessCardRequestDto.UpdateMemberBusinessCardRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (memberBusinessCardRepository.findByMemberIdxAndBusinessCardIdx(memberIdx, businessCardIdx).isEmpty()) {
            throw new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND);
        }

        MemberBusinessCard memberBusinessCard = memberBusinessCardRepository.findByMemberIdxAndBusinessCardIdx(memberIdx, businessCardIdx)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BUSINESS_CARD_NOT_FOUND));

        memberBusinessCard.setMemo(requestDto.getMemo());
        memberBusinessCardRepository.update(memberBusinessCard);

        return MemberBusinessCardResponseDto.MemberBusinessCardTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(businessCardIdx)
                .build();
    }



}
