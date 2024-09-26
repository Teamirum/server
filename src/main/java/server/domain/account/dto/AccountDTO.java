package server.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

    private Long idx;                // 계좌 ID
    private Long memberIdx;          // 회원 ID
    private String accountHolderName; // 계좌 소유자 이름
    private Integer amount;           // 잔액
    private String bankName;          // 은행 이름
    private String accountNumber;     // 계좌 번호
    private LocalDateTime createdAt;  // 생성 일자
}
