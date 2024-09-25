package server.domain.market.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Market {

    private Long idx;

    private Long memberIdx;

    private String accountHolderName;

    private int amount;

    private String bankName;

    private String accountNumber;

    private LocalDateTime createdAt;


}
