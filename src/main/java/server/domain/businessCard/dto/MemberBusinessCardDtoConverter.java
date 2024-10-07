package server.domain.businessCard.dto;

import server.domain.businessCard.domain.MemberBusinessCard;

import java.util.List;

public class MemberBusinessCardDtoConverter {

    public static MemberBusinessCardResponseDto.MemberBusinessCardInfoResponseDto convertToMemberBusinessCardInfoResponseDto(MemberBusinessCard memberBusinessCard) {
        return MemberBusinessCardResponseDto.MemberBusinessCardInfoResponseDto.builder()
                .idx(memberBusinessCard.getIdx())
                .memberIdx(memberBusinessCard.getMemberIdx())
                .businessCardIdx(memberBusinessCard.getBusinessCardIdx())
                .status(memberBusinessCard.getStatus())
                .memo(memberBusinessCard.getMemo())
                .build();
    }

    public static MemberBusinessCardResponseDto.MemberBusinessCardListResponseDto convertToMemberBusinessCardListResponseDto(List<MemberBusinessCard> memberBusinessCardList) {
        if (memberBusinessCardList == null) {
            return MemberBusinessCardResponseDto.MemberBusinessCardListResponseDto.builder()
                    .cnt(0)
                    .isSuccess(false)
                    .build();
        }

        return MemberBusinessCardResponseDto.MemberBusinessCardListResponseDto.builder()
                .cnt(memberBusinessCardList.size())
                .memberBusinessCardList(memberBusinessCardList.stream()
                        .map(MemberBusinessCardDtoConverter::convertToMemberBusinessCardInfoResponseDto)
                        .toList())
                .isSuccess(true)
                .build();
    }
}
