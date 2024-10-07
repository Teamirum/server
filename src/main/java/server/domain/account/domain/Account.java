package server.domain.account.domain;

import lombok.*;

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

    private String accountSecret;

}
