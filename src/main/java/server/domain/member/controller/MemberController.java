package server.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.member.dto.MemberRequestDto;
import server.domain.member.service.MemberService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.security.service.JwtService;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    @PostMapping("/signUp")
    public ApiResponse<?> signUp(@RequestBody MemberRequestDto.MemberSignupRequestDto requestDto
//                                 ,@RequestParam(value = "authToken") String authToken
    ) {
        log.info("회원가입 요청 : memberId = {}", requestDto.getMemberId());
        // 개발 단계 에서는 임시 비활성화
//        if (authToken == null) {
//            return ApiResponse.onFailure(ErrorStatus.MEMBER_PHONE_TOKEN_NOT_PROVIDED.getCode(), ErrorStatus.MEMBER_PHONE_TOKEN_NOT_PROVIDED.getMessage(), null);
//        }
//        if (!jwtService.isValidPhoneAuthToken(authToken, requestDto.getPhoneNum())) {
//            return ApiResponse.onFailure(ErrorStatus.MEMBER_PHONE_AUTH_NOT_VALID.getCode(), ErrorStatus.MEMBER_PHONE_AUTH_NOT_VALID.getMessage(), "제공된 authToken 이 유효하지 않습니다.");
//        }
        return ApiResponse.onSuccess(memberService.signUp(requestDto));
    }

    @GetMapping("/test")
    public ApiResponse<?> test() {
        log.info("test");
        return ApiResponse.onSuccess("test");
    }
}
