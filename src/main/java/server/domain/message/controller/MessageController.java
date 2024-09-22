package server.domain.message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.message.dto.MessageRequestDto;
import server.domain.message.service.MessageService;
import server.global.apiPayload.ApiResponse;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/send")
    public ApiResponse<?> sendMessage(@RequestParam(value = "phoneNum") String phoneNum) {
        log.info("인증번호 전송 요청 : phoneNum = {}", phoneNum);
        return ApiResponse.onSuccess(messageService.sendMessage(phoneNum));
    }

    @GetMapping("/confirm")
    public ApiResponse<?> confirmMessage(@RequestBody MessageRequestDto.CheckMessageRequestDto requestDto) {
        log.info("인증번호 확인 요청 : phoneNum = {}", requestDto.getPhoneNum());
        return ApiResponse.onSuccess(messageService.checkMessage(requestDto.getPhoneNum(), requestDto.getAuthNum()));
    }
}
