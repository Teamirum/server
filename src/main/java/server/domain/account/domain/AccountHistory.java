package server.domain.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountHistory {

    private Long idx;

    private Long accountIdx;

    private AccountHistoryType accountHistoryType;

    private LocalDateTime createdAt;

    private Integer amount;

    private Integer remainAmount;

    private String name;

    public enum AccountHistoryType {
        SEND, GET
    }
}

