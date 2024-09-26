package server.domain.credit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credit {

    private Long idx;

    private Long memberIdx;

    private String creditNumber;

    private String creditName;

    private String companyName;

    private String creditSecret;

    private int amountSum;

    private String imgUrl;

    private String expirationDate;

    private LocalDateTime createdAt;
}
