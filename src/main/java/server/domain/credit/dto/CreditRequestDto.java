package server.domain.credit.dto;

import lombok.Builder;
import lombok.Data;

public class CreditRequestDto {

    @Data
    @Builder
    public static class UploadCreditRequestDto {
        public String creditNumber;
        public String creditName;
        public String companyName;
        public String creditSecret;
        public String imgUrl;
    }

}
