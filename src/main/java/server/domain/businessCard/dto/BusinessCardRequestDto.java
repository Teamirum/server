package server.domain.businessCard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class BusinessCardRequestDto {

    @Data
    public static class UploadBusinessCardRequestDto {
        public Long memberIdx;
        public String name;
        public String phoneNum;
        public String email;
        public String position;
        public String part;
        public String company;
        public String address;
        public String faxNum;
        public String memo;

        @JsonCreator
        public UploadBusinessCardRequestDto(
                @JsonProperty("memberIdx") Long memberIdx,
                @JsonProperty("name") String name,
                @JsonProperty("phoneNum") String phoneNum,
                @JsonProperty("email") String email,
                @JsonProperty("position") String position,
                @JsonProperty("part") String part,
                @JsonProperty("company") String company,
                @JsonProperty("address") String address,
                @JsonProperty("faxNum") String faxNumber,
                @JsonProperty("memo") String memo
        ) {
            this.memberIdx = memberIdx;
            this.name = name;
            this.phoneNum = phoneNum;
            this.email = email;
            this.position = position;
            this.part = part;
            this.company = company;
            this.address = address;
            this.faxNum = faxNum;
            this.memo = memo;
        }

    }

    @Data
    public static class UpdateBusinessCardRequestDto {
        public Long idx;
        public String name;
        public String phoneNum;
        public String email;
        public String position;
        public String part;
        public String company;
        public String address;
        public String faxNum;

        @JsonCreator
        public UpdateBusinessCardRequestDto(
                @JsonProperty("idx") Long idx,
                @JsonProperty("name") String name,
                @JsonProperty("phoneNum") String phoneNum,
                @JsonProperty("email") String email,
                @JsonProperty("position") String position,
                @JsonProperty("part") String part,
                @JsonProperty("company") String company,
                @JsonProperty("address") String address,
                @JsonProperty("faxNum") String faxNum
        ) {
            this.idx = idx;
            this.name = name;
            this.phoneNum = phoneNum;
            this.email = email;
            this.position = position;
            this.part = part;
            this.company = company;
            this.address = address;
            this.faxNum = faxNum;
        }
    }
}
