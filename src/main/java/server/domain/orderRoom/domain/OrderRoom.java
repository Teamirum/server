package server.domain.orderRoom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.orderRoom.model.OrderRoomStatus;
import server.domain.orderRoom.model.OrderRoomType;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 6494678977089006639L;

//    private Long idx;

    private Long orderIdx;

    private Long ownerMemberIdx;

    private int maxMemberCnt;

    private int memberCnt;

    private List<Long> memberIdxList;

    private int totalPrice;

    private int currentPrice;

    private OrderRoomType type;

    private HashMap<Long, List<Long>> menuSelect;

    private HashMap<Long, Integer> menuAmount;

    private int readyCnt;

    private OrderRoomStatus status;

    private String createdAt;

    private String imgUrl;

    public void updateCurrentPrice(int price) {
        currentPrice += price;
    }

    public boolean enterMember(Long memberIdx) {
        if (memberCnt >= maxMemberCnt) {
            return false;
        }
        memberCnt++;
        memberIdxList.add(memberIdx);
        return true;
    }

    public boolean exitMember(Long memberIdx) {
        if (memberCnt <= 0) {
            return false;
        }
        if (!memberIdxList.contains(memberIdx)) {
            return false;
        }
        memberCnt--;
        memberIdxList.remove(memberIdx);
        return true;
    }

    public boolean readyToPay() {
        if (readyCnt >= maxMemberCnt) {
            return false;
        }
        readyCnt++;
        return true;
    }

    public boolean cancelReadyToPay() {
        if (readyCnt <= 0) {
            return false;
        }
        readyCnt--;
        return true;
    }

}
