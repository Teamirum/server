package server.domain.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    private Long idx;

    private Long memberIdx;

    private String accountHolderName;

    private Integer amount;

    private String bankName;

    private String accountNumber;

    private LocalDateTime createdAt;

    

    // 입금
//    public void deposit(int amount) {
//        this.amount += amount;
//    }
//
//    // 출금
//    public void withdraw(int amount) {
//        if (this.amount >= amount) {
//            this.amount -= amount;
//        } else {
//            throw new IllegalArgumentException("Insufficient balance.");
//        }
//    }

}
