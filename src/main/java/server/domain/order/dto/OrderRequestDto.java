package server.domain.order.dto;

public class OrderRequestDto {

    public static class CreateOrderRequestDto {
        public Long marketIdx;
        public String name;
        public int amount;
    }
}
