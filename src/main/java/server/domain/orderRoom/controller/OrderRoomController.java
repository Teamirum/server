package server.domain.orderRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.domain.orderRoom.dto.OrderRoomRequestDto.*;
import server.domain.orderRoom.service.OrderRoomService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderRoomController {

    private final OrderRoomService orderRoomService;

    @PostMapping("/api/order/room/create")
    public ApiResponse<?> createOrderRoom(@RequestBody CreateOrderRoomRequestDto requestDto) {
        String memberId = getLoginMemberId();
        log.info("createOrderRoom : memberId = {}", memberId);
        return ApiResponse.onSuccess(orderRoomService.createOrderRoom(requestDto, memberId));
    }

    @MessageMapping("/order/room/enter")
    public void enterOrderRoom(@Payload EnterOrderRoomRequestDto requestDto, Principal principal) {
        String memberId = principal.getName();
        log.info("enterOrderRoom : orderIdx = {}, memberId = {}", requestDto.getOrderIdx(), memberId);
        orderRoomService.enterOrderRoom(requestDto.getOrderIdx(), memberId);
    }

    @MessageMapping("/order/room/select")
    public void selectOrderMenu(@Payload SelectMenuRequestDto requestDto, Principal principal) {
        String memberId = principal.getName();
        log.info("selectOrderMenu : orderIdx = {}, memberId = {}", requestDto.getOrderIdx(), memberId);
        orderRoomService.selectOrderMenu(requestDto, memberId);
    }

    @MessageMapping("/order/room/cancel")
    public void cancelOrderMenu(@Payload SelectMenuRequestDto requestDto, Principal principal) {
        String memberId = principal.getName();
        orderRoomService.cancelOrderMenu(requestDto, memberId);
    }

    @MessageMapping("/order/room/price")
    public void selectPrice(@Payload SelectPriceRequestDto requestDto, Principal principal) {
        String memberId = principal.getName();
        orderRoomService.selectPrice(requestDto, memberId);
    }

    @MessageMapping("/order/room/ready/{orderIdx}")
    public void readyOrderRoom(@DestinationVariable Long orderIdx, Principal principal) {
        String memberId = principal.getName();
        orderRoomService.readyToPay(orderIdx, memberId);
    }

    @MessageMapping("/order/room/ready/cancel/{orderIdx}")
    public void cancelReadyOrderRoom(@DestinationVariable Long orderIdx, Principal principal) {
        String memberId = principal.getName();
        orderRoomService.cancelReadyToPay(orderIdx, memberId);
    }

    public String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
