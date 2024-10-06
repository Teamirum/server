package server.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    private Long idx;

    private Long marketIdx;

    private String name;

    private int totalPrice;

    private int taxFreePrice;

    private int vatPrice;

    private int tableNumber;

    private LocalDateTime createdAt;

    public void updatePrice(int totalPrice) {
        this.totalPrice = totalPrice;
        this.taxFreePrice =  Math.round(totalPrice * 0.9f);
        this.vatPrice = totalPrice - taxFreePrice;
    }

}
