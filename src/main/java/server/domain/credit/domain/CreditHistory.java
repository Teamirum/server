package server.domain.credit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditHistory {

    private Long idx;

    private Long creditIdx;

    private LocalDateTime createdAt;

    private Long amount;

    private Long amountSum;

    private String name;

}
