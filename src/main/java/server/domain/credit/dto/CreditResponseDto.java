package server.domain.credit.dto;

import lombok.Builder;
import lombok.Data;

public class CreditResponseDto {

    @Data
    @Builder
    public static class CreditTaskSuccessResponseDto {
        public Boolean isSuccess;
        public Long idx;
    }
}
