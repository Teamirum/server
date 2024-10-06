package server.domain.pay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.account.service.AccountService;
import server.domain.credit.service.CreditService;
import server.domain.pay.dto.PayRequestDto;
import server.domain.pay.dto.PayResponseDto;
import server.domain.pay.repository.PayRepository;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final CreditService creditService;
    private final AccountService accountService;

    // 계좌, 카드 통합조회
    public PayResponseDto.PayMethodResponseDto getAllPayMethod(String memberId) {
        return PayResponseDto.PayMethodResponseDto.builder()
                .credit(creditService.getCreditList(memberId))
                .account(accountService.getAccountList(memberId))
                .build();
    }

    public String startPay(PayRequestDto.StartPayRequestDto requestDto, String memberId) {
        // 결제 로직
        return "결제 성공";
    }
}
