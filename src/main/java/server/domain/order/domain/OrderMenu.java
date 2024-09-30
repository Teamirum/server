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
public class OrderMenu {

    private Long idx;

    private Long orderIdx;

    private Long menuIdx;

    private int price;

    private int amount;

}
