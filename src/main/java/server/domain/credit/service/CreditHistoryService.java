package server.domain.credit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.credit.domain.Credit;
import server.domain.credit.dto.CreditHistoryRequestDto;
import server.domain.credit.domain.CreditHistory;
import server.domain.credit.mapper.CreditHistoryMapper;
import server.domain.credit.mapper.CreditMapper;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreditHistoryService {

    private final CreditMapper creditMapper;
    private final CreditHistoryMapper creditHistoryMapper;

    public ApiResponse<?> insertCreditHistory(CreditHistoryRequestDto.UploadCreditHistoryRequestDto requestDto, String loginMemberId) {
        // creditIdx를 이용해 Credit 테이블에서 데이터 조회
        Map<String, Object> params = new HashMap<>();
        params.put("idx", requestDto.getCreditIdx());
        params.put("memberIdx", loginMemberId);
        System.out.println(loginMemberId);
        Credit credit = creditMapper.findByIdxAndMemberIdx(params);

        if (credit == null) {
            throw new ErrorHandler(ErrorStatus.CREDIT_NOT_AUTHORIZED); // Credit이 없는 경우 예외 처리
        }

        CreditHistory creditHistory = new CreditHistory();
        creditHistory.setCreditIdx(credit.getIdx());
        creditHistory.setAmount(requestDto.getAmount());
        creditHistory.setAmountSum(requestDto.getAmountSum());
        creditHistory.setName(requestDto.getName());

        creditHistoryMapper.insertCreditHistory(creditHistory);

        return ApiResponse.onSuccess("신용카드 결제 내역이 성공적으로 삽입되었습니다.");
    }

}
