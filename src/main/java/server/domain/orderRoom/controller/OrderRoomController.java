package server.domain.orderRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;
import server.domain.orderRoom.dto.OrderRoomRequestDto.*;
import server.domain.orderRoom.service.OrderRoomService;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderRoomController {

    private final OrderRoomService orderRoomService;

    @MessageMapping("/order/room/enter/{orderIdx}")
    public void enterOrderRoom(@DestinationVariable Long orderIdx) {
        orderRoomService.enterOrderRoom(orderIdx, getLoginMemberId());
    }


    @MessageMapping("/order/room//select")
    public void selectOrderMenu(@Payload SelectMenuRequestDto requestDto) {
        String memberId = getLoginMemberId();
        orderRoomService.selectOrderMenu(requestDto, memberId);
    }

    @MessageMapping("/order/room//unselect")
    public void cancelOrderMenu(@Payload SelectMenuRequestDto requestDto) {
        String memberId = getLoginMemberId();
        orderRoomService.cancelOrderMenu(requestDto, memberId);
    }




    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
