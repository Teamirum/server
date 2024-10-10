package server.domain.pay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.account.domain.Account;
import server.domain.account.repository.AccountRepository;
import server.domain.account.service.AccountService;
import server.domain.credit.domain.Credit;
import server.domain.credit.repository.CreditRepository;
import server.domain.credit.service.CreditService;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.domain.order.domain.Order;
import server.domain.order.domain.TogetherOrder;
import server.domain.order.repository.OrderRepository;
import server.domain.order.repository.TogetherOrderRepository;
import server.domain.pay.domain.Pay;
import server.domain.pay.dto.PayDtoConverter;
import server.domain.pay.dto.PayRequestDto;
import server.domain.pay.dto.PayResponseDto.*;
import server.domain.pay.model.PayMethod;
import server.domain.pay.model.PayStatus;
import server.domain.pay.model.PayType;
import server.domain.pay.repository.PayRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final MemberRepository memberRepository;
    private final TogetherOrderRepository togetherOrderRepository;

    private final OrderRepository orderRepository;
    private final CreditService creditService;
    private final AccountService accountService;

    // 계좌, 카드 통합조회
    public PayMethodResponseDto getAllPayMethod(String memberId) {
        return PayMethodResponseDto.builder()
                .credit(creditService.getCreditList(memberId))
                .account(accountService.getAccountList(memberId))
                .build();
    }

    public PaySuccessResponseDto startTogetherPay(PayRequestDto.StartPayRequestDto requestDto, String memberId) {
        Member member = getMember(memberId);
        Order order = orderRepository.findByOrderIdx(requestDto.getOrderIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_NOT_FOUND));

        // 함께 결제 로직
        TogetherOrder togetherOrder = togetherOrderRepository.findByMemberIdxAndOrderIdx(member.getIdx() ,requestDto.getOrderIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_MEMBER_PARTICIPANT_ROOM_NOT_FOUND));
        if (requestDto.getPayMethod().equals("CREDIT")) {
            Credit credit = creditService.getCreditByIdx(requestDto.getCreditIdx());
            if (creditService.isAbleToUseCredit(credit)) {
                creditService.payWithCredit(credit, togetherOrder.getPrice(), order.getName());
            }
            Pay pay = Pay.builder()
                    .orderIdx(order.getIdx())
                    .memberIdx(member.getIdx())
                    .creditIdx(requestDto.getCreditIdx())
                    .price(togetherOrder.getPrice())
                    .tid("TOGETHER" + order.getIdx() + member.getIdx())
                    .payMethod(PayMethod.fromName(requestDto.getPayMethod()))
                    .payType(PayType.TOGETHER)
                    .payStatus(PayStatus.ACCEPT)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
            payRepository.save(pay);

            Pay savedPay = payRepository.findByOrderIdxAndMemberIdx(order.getIdx(), member.getIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.PAY_SAVE_FAIL));

            return PayDtoConverter.convertToPaySuccessResponseDto(savedPay, credit.getCreditName(), credit.getCreditNumber());
        }

        else if (requestDto.getPayMethod().equals("ACCOUNT")) {
            Account account = accountService.getAccountByIdx(requestDto.getAccountIdx());
            if (accountService.isAbleToUseAccount(account, togetherOrder.getPrice())) {
                accountService.payWithAccount(account, order.getTotalPrice());
            }
            Pay pay = Pay.builder()
                    .orderIdx(order.getIdx())
                    .memberIdx(member.getIdx())
                    .accountIdx(requestDto.getAccountIdx())
                    .price(order.getTotalPrice())
                    .tid("ALONE" + order.getIdx() + member.getIdx())
                    .payMethod(PayMethod.fromName(requestDto.getPayMethod()))
                    .payType(PayType.TOGETHER)
                    .payStatus(PayStatus.ACCEPT)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
            payRepository.save(pay);

            Pay savedPay = payRepository.findByOrderIdxAndMemberIdx(order.getIdx(), member.getIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.PAY_SAVE_FAIL));

            return PayDtoConverter.convertToPaySuccessResponseDto(savedPay, account.getBankName(), account.getAccountNumber());
        }
        throw new ErrorHandler(ErrorStatus.PAY_METHOD_NOT_FOUND);

    }

    public PaySuccessResponseDto startAlonePay(PayRequestDto.StartPayRequestDto requestDto, String memberId) {
        Member member = getMember(memberId);
        Order order = orderRepository.findByOrderIdx(requestDto.getOrderIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_NOT_FOUND));

        if (requestDto.getPayMethod().equals("CREDIT")) {
            Credit credit = creditService.getCreditByIdx(requestDto.getCreditIdx());
            if (creditService.isAbleToUseCredit(credit)) {
                creditService.payWithCredit(credit, order.getTotalPrice(), order.getName());
            }
            Pay pay = Pay.builder()
                    .orderIdx(order.getIdx())
                    .memberIdx(member.getIdx())
                    .creditIdx(requestDto.getCreditIdx())
                    .price(order.getTotalPrice())
                    .tid(order.getIdx() + " " + member.getIdx())
                    .payMethod(PayMethod.fromName(requestDto.getPayMethod()))
                    .payType(PayType.ALONE)
                    .payStatus(PayStatus.ACCEPT)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
            payRepository.save(pay);

            Pay savedPay = payRepository.findByOrderIdxAndMemberIdx(order.getIdx(), member.getIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.PAY_SAVE_FAIL));

            return PayDtoConverter.convertToPaySuccessResponseDto(savedPay, credit.getCreditName(), credit.getCreditNumber());
        } else if (requestDto.getPayMethod().equals("ACCOUNT")) {
            Account account = accountService.getAccountByIdx(requestDto.getAccountIdx());
            if (accountService.isAbleToUseAccount(account, order.getTotalPrice())) {
                accountService.payWithAccount(account, order.getTotalPrice());
            }
            Pay pay = Pay.builder()
                    .orderIdx(order.getIdx())
                    .memberIdx(member.getIdx())
                    .accountIdx(requestDto.getAccountIdx())
                    .price(order.getTotalPrice())
                    .tid(order.getIdx() + " " + member.getIdx())
                    .payMethod(PayMethod.fromName(requestDto.getPayMethod()))
                    .payType(PayType.ALONE)
                    .payStatus(PayStatus.ACCEPT)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
            payRepository.save(pay);

            Pay savedPay = payRepository.findByOrderIdxAndMemberIdx(order.getIdx(), member.getIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.PAY_SAVE_FAIL));

            return PayDtoConverter.convertToPaySuccessResponseDto(savedPay, account.getBankName(), account.getAccountNumber());
        }
        throw new ErrorHandler(ErrorStatus.PAY_METHOD_NOT_FOUND);
    }
    private Member getMember(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
