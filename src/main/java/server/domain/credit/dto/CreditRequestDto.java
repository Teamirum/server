package server.domain.credit.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

public class CreditRequestDto {

    @Data
    public static class UploadCreditRequestDto {
        public String creditNumber;
        public String creditName;
        public String companyName;
        public String creditSecret;
        public String expirationDate;
        public String imgUrl;

        @JsonCreator
        public UploadCreditRequestDto(
                @JsonProperty("creditNumber") String creditNumber,
                @JsonProperty("creditName") String creditName,
                @JsonProperty("companyName") String companyName,
                @JsonProperty("creditSecret") String creditSecret,
                @JsonProperty("expirationDate") String expirationDate,
                @JsonProperty("imgUrl") String imgUrl
        ) {
            this.creditNumber = creditNumber;
            this.creditName = creditName;
            this.companyName = companyName;
            this.creditSecret = creditSecret;
            this.expirationDate = expirationDate;
            this.imgUrl = imgUrl;
        }
    }

    @Data
    public static class PayCreditRequestDto {
        private String creditNumber;
        private Integer amountSum;
        private String name;

        @JsonCreator
        public PayCreditRequestDto(
                @JsonProperty("creditNumber") String creditNumber,
                @JsonProperty("amountSum") Integer amountSum,
                @JsonProperty("name") String name
        ) {
            this.creditNumber = creditNumber;
            this.amountSum = amountSum;
            this.name = name;
        }
    }

}
