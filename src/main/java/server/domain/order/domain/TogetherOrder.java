package server.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.order.model.TogetherOrderStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TogetherOrder {

    private Long idx;

    private Long orderIdx;

    private Long memberIdx;

    private TogetherOrderStatus status;

    private int price;

    private LocalDateTime createdAt;
}
