package server.domain.pay.dto;

import server.domain.account.domain.Account;
import server.domain.credit.domain.Credit;
import server.domain.order.domain.Order;
import server.domain.pay.domain.Pay;
import server.domain.pay.dto.PayResponseDto.PaySuccessResponseDto;
import server.domain.pay.dto.PayResponseDto.TogetherPayInfoResponseDto;
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

    public static TogetherPayInfoResponseDto convertToTogetherPayInfoResponseDto(Pay pay, Order order, Account account, Credit credit) {
        TogetherPayInfoResponseDto dto = TogetherPayInfoResponseDto.builder()
                .idx(pay.getIdx())
                .orderIdx(order.getIdx())
                .marketIdx(order.getMarketIdx())
                .price(pay.getPrice())
                .payMethod(pay.getPayMethod().name())
                .tid(pay.getTid())
                .orderName(order.getName())
                .payStatus(pay.getPayStatus().name())
                .payType(pay.getPayType().name())
                .createdAt(pay.getCreatedAt().toString())
                .isSuccess(true)
                .build();
        if (credit != null) {
            dto.setCreditName(credit.getCreditName());
            dto.setCreditNumber(credit.getCreditNumber());
        } else {
            dto.setBankName(account.getBankName());
            dto.setAccountNumber(account.getAccountNumber());
        }
        return dto;
    }
}
