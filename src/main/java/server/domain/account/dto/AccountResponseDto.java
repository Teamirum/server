package server.domain.account.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class AccountResponseDto {

    @Data
    @Builder
    public static class AccountTaskSuccessResponseDto {
        public boolean isSuccess;
        public Long idx;
    }

    @Data
    @Builder
    public static class AccountListResponseDto {
        public int cnt;
        List<AccountInfoResponseDto> accountList;
        public boolean isSuccess;
    }

    @Data
    @Builder
    public static class AccountInfoResponseDto {
        public Long idx;
        public String accountHolderName;
        public Integer amount;
        public String bankName;
        public String accountNumber;
        public String createdAt;
    }
}
