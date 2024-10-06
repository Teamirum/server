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
public class TogetherOrder {

    private Long idx;

    private Long orderIdx;

    private Long memberIdx;

    private int price;

    private LocalDateTime createdAt;
}
