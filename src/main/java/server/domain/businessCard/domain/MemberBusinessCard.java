package server.domain.businessCard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberBusinessCard {

    private Long idx;

    private Long memberIdx;

    private Long businessCardIdx;

    private String status;

    private String memo;
}
