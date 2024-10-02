package server.domain.orderRoom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.orderRoom.model.OrderRoomStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRoom implements Serializable {

    private Long idx;

    private Long orderIdx;

    private Long ownerMemberIdx;

    private int maxMemberCnt;

    private int memberCnt;

    private int amount;

    private OrderRoomStatus status;

    private LocalDateTime createdAt;
}
