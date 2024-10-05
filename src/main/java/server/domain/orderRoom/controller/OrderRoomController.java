package server.domain.orderRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @MessageMapping("/order/room/enter")
    public void enterOrderRoom(@Payload EnterOrderRoomRequestDto requestDto) {
        orderRoomService.enterOrderRoom(requestDto, getLoginMemberId());
    }

    @MessageMapping("/order/room/start")
    public void startSelectOrderMenu(@Payload StartSelectOrderMenuRequestDto requestDto) {
        orderRoomService.startSelectOrderMenu(requestDto, getLoginMemberId());
    }


    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
