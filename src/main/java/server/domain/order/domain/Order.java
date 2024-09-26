package server.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    private Long idx;

    private Long marketIdx;

    private String name;

    private int amount;

    private int taxFreeAmount;

    private int vatAmount;

    private Long orderMemberIdx;

    private LocalDateTime createdAt;

}
