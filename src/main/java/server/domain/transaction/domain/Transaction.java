package server.domain.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String accountNumber;

    private String creditNumber;

    private LocalDateTime time;

    private PayMethod payMethod;

    private int amount;

    private String memo;

    private Category category;

    private String tranId;

    // 결제 방식 ENUM
    public enum PayMethod {
        CARD, ACCOUNT
    }

    public enum Category {
        SALARY,
        INTEREST,
        ALLOWANCE,
        FOOD,
        SHOPPING,
        TRANSPORT,
        ENTERTAINMENT,
        COMMUNICATION,
        UNCATEGORIZED
    }





}
