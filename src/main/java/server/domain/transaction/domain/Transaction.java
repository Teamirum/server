package server.domain.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    private Long idx;

    private Long memberIdx;

    private Long creditIdx;

    private Long accountIdx;

    private LocalDateTime time;

    private PayMethod payMethod;  // 결제 방식 (ENUM)

    private int amount;

    private String memo;

    private Category category;  // 거래 카테고리 (ENUM)

    private String tranId;

    // 결제 방식 ENUM
    public enum PayMethod {
        CARD, ACCOUNT, NPAY, KPAY, TogetherPay
    }

    // 카테고리 ENUM
    public enum Category {
        FOOD, TRANSPORT, SHOPPING, ETC
    }
}
