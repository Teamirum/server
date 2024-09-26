package server.domain.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.domain.order.dto.OrderRequestDto;
import server.domain.order.service.OrderService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<?> createOrder(@RequestBody OrderRequestDto.CreateOrderRequestDto requestDto) {
        String memberId = getLoginMemberId();
        log.info("주문 생성 요청 : memberId = {}", memberId);
        return ApiResponse.onSuccess(orderService.createOrder(requestDto, memberId));
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
