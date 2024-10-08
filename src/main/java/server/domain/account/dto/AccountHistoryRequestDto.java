package server.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import server.domain.account.domain.AccountHistory;
import lombok.Data;

public class AccountHistoryRequestDto {

    @Data
    public static class UploadAccountHistoryRequestDto {
        public Long accountIdx;
        public AccountHistory.AccountHistoryType accountHistoryType;
        public Long amount;
        public Long remainAmount;
        public String name;

      @JsonCreator
        public UploadAccountHistoryRequestDto(
                @JsonProperty("accountIdx") Long accountIdx,
                @JsonProperty("accountHistoryType") String accountHistoryType,
                @JsonProperty("amount") Long amount,
                @JsonProperty("remainAmount") Long remainAmount,
                @JsonProperty("name") String name
      ) {
          this.accountIdx = accountIdx;
          this.accountHistoryType = AccountHistory.AccountHistoryType.valueOf(accountHistoryType);
          this.amount = amount;
          this.remainAmount = remainAmount;
          this.name = name;
      }
    }

    @Data
    public static class UpdateAccountHistoryRequestDto {
        public Long idx;
        public Long accountIdx;
        public AccountHistory.AccountHistoryType accountHistoryType;
        public Long amount;
        public Long remainAmount;
        public String name;

        @JsonCreator
        public UpdateAccountHistoryRequestDto(
                @JsonProperty("idx") Long idx,
                @JsonProperty("accountIdx") Long accountIdx,
                @JsonProperty("accountHistoryType") String accountHistoryType,
                @JsonProperty("amount") Long amount,
                @JsonProperty("remainAmount") Long remainAmount,
                @JsonProperty("name") String name
        ) {
            this.idx = idx;
            this.accountIdx = accountIdx;
            this.accountHistoryType = AccountHistory.AccountHistoryType.valueOf(accountHistoryType);
            this.amount = amount;
            this.remainAmount = remainAmount;
            this.name = name;
        }
    }


    public static class DeleteAccountHistoryRequestDto {
        public Long idx;

        public DeleteAccountHistoryRequestDto(Long idx) {
            this.idx = idx;
        }
    }
}


