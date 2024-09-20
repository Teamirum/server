package server.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.member.dto.MemberRequestDto;
import server.domain.member.service.MemberService;
import server.global.apiPayload.ApiResponse;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ApiResponse<?> signUp(@RequestBody MemberRequestDto.MemberSignupRequestDto requestDto) {
        memberService.signUp(requestDto);
        return ApiResponse.onSuccess("signUp");
    }

    @GetMapping("/test")
    public ApiResponse<?> test() {
        log.info("test");
        return ApiResponse.onSuccess("test");
    }
}
