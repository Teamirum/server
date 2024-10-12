package server.domain.member.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.PATCH;
import server.domain.member.dto.MemberRequestDto;
import server.domain.member.service.MemberService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.auth.security.service.JwtService;
import server.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    @PostMapping("/signUp")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "인증토큰", required = true),
    })
    @ApiOperation(value="회원가입")
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

    @GetMapping("/my")
    public ApiResponse<?> getMyInfo() {
        String loginMemberId = getLoginMemberId();
        log.info("내 정보 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(memberService.getMyInfo(loginMemberId));
    }

    @PatchMapping("/connect")
    public ApiResponse<?> connectMyData() {
        String loginMemberId = getLoginMemberId();
        log.info("마이데이터 연동 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(memberService.connectMyData(loginMemberId));
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    //is_connected가 true인지 false인지 확인하는 api.
    @GetMapping("/connect/check")
    public ApiResponse<?> checkConnectMyData() {
        String loginMemberId = getLoginMemberId();
        log.info("마이데이터 연동 확인 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(memberService.checkConnectMyData(loginMemberId));
    }

}
