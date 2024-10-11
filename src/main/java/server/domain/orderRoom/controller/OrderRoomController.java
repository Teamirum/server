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
    public void enterOrderRoom(@Payload EnterOrderRoomRequestDto requestDto) {
        log.info("enterOrderRoom : orderIdx = {}, memberId = {}", requestDto.getOrderIdx(), requestDto.getMemberId());
        orderRoomService.enterOrderRoom(requestDto.getOrderIdx(), requestDto.getMemberId());
    }

    @MessageMapping("/order/room/select")
    public void selectOrderMenu(@Payload SelectMenuRequestDto requestDto) {
        log.info("selectOrderMenu : orderIdx = {}, memberId = {}", requestDto.getOrderIdx(), requestDto.getMemberId());
        orderRoomService.selectOrderMenu(requestDto, requestDto.getMemberId());
    }

    @MessageMapping("/order/room/cancel")
    public void cancelOrderMenu(@Payload SelectMenuRequestDto requestDto) {
        log.info("cancelOrderMenu : orderIdx = {}, memberId = {}", requestDto.getOrderIdx(), requestDto.getMemberId());
        orderRoomService.cancelOrderMenu(requestDto, requestDto.getMemberId());
    }

    @MessageMapping("/order/room/price")
    public void selectPrice(@Payload SelectPriceRequestDto requestDto) {
        log.info("selectPrice : orderIdx = {}, memberId = {}", requestDto.getOrderIdx(), requestDto.getMemberId());
        orderRoomService.selectPrice(requestDto, requestDto.getMemberId());
    }

    @MessageMapping("/order/room/ready")
    public void readyOrderRoom(@Payload ReadyOrderRoomRequestDto requestDto) {
        log.info("readyOrderRoom : orderIdx = {}, memberId = {}", requestDto.getOrderIdx(), requestDto.getMemberId());
        orderRoomService.readyToPay(requestDto.getOrderIdx(), requestDto.getMemberId());
    }

    @MessageMapping("/order/room/ready/cancel")
    public void cancelReadyOrderRoom(@Payload ReadyOrderRoomRequestDto requestDto) {
        log.info("cancelReadyOrderRoom : orderIdx = {}, memberId = {}", requestDto.getOrderIdx(), requestDto.getMemberId());
        orderRoomService.cancelReadyToPay(requestDto.getOrderIdx(), requestDto.getMemberId());
    }

    public String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
