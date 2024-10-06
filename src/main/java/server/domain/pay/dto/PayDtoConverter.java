package server.domain.pay.dto;

import server.domain.pay.domain.Pay;
import server.domain.pay.dto.PayResponseDto.PaySuccessResponseDto;
import server.domain.pay.model.PayMethod;

public class PayDtoConverter {
    public static PaySuccessResponseDto convertToPaySuccessResponseDto(Pay pay, String name, String number) {
        PaySuccessResponseDto dto = PaySuccessResponseDto.builder()
                .idx(pay.getIdx())
                .memberIdx(pay.getMemberIdx())
                .orderIdx(pay.getOrderIdx())
                .price(pay.getPrice())
                .payType(pay.getPayType().name())
                .payStatus(pay.getPayStatus().name())
                .createdAt(pay.getCreatedAt().toString())
                .isSuccess(true)
                .build();
        if (pay.getPayMethod() == PayMethod.CREDIT) {
            dto.setCreditName(name);
            dto.setCreditNumber(number);
        } else {
            dto.setBankName(name);
            dto.setAccountNumber(number);
        }
        return dto;
    }
}
