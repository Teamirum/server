package server.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMenu {

    private Long idx;

    private Long orderIdx;

    private Long menuIdx;

    private String menuName;

    private int price;

    private int amount;

    private String imgUrl;

    public String getQrData() {
        String baseUrl = "http://localhost:5173/payment-waiting"; // 기본 URL
        String queryParam = "?idx=" + this.orderIdx; // idx를 쿼리 파라미터로 추가
        String fullUrl = baseUrl + queryParam; // 완전한 URL 생성

        return new String(String.format(
                "{\"url\":\"%s\"}", fullUrl
        ).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }


}
