package server.domain.pay.domain;

import server.domain.pay.model.PayMethod;
import server.domain.pay.model.PayStatus;
import server.domain.pay.model.PayType;

import java.time.LocalDateTime;

public class Pay {

    private Long idx;

    private Long memberIdx;

    private Long orderIdx;

    private Long creditIdx;

    private Long accountIdx;

    private PayMethod payMethod;

    private String tid;

    private int price;

    private PayStatus payStatus;

    private PayType payType;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
