package server.domain.businessCard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class BusinessCardResponseDto {

    @Data
    @Builder
    public static class BusinessCardTaskSuccessResponseDto {
        public Long idx;
        public String imgUrl;
        public Boolean isSuccess;
    }

    @Data
    @Builder
    public static class BusinessCardListResponseDto {
        public int cnt;
        List<BusinessCardInfoResponseDto> businessCardList;
        public boolean isSuccess;
    }

    @Data
    @Builder
    public static class BusinessCardInfoResponseDto {
        public Long idx;
        public String name;
        public String phoneNumber;
        public String tel_num;
        public String email;
        public String position;
        public String part;
        public String company;
        public String address;
        public String imgurl;
        public String qrData;
    }
}
