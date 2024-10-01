package server.domain.businessCard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.businessCard.dto.BusinessCardRequestDto;
import server.domain.businessCard.service.BusinessCardService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/businessCard")
@Slf4j
@RequiredArgsConstructor
public class BusinessCardController {

    private final BusinessCardService businessCardService;

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @PostMapping
    public ApiResponse<?> uploadBusinessCard(@RequestBody BusinessCardRequestDto.UploadBusinessCardRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("명함 업로드 요청 : 현재loginMemberId = {}, businessName = {}", loginMemberId ,requestDto.name);
        return ApiResponse.onSuccess(businessCardService.upload(requestDto, loginMemberId));
    }

    @GetMapping("/all")
    public ApiResponse<?> getBusinessCardList() {
        String loginMemberId = getLoginMemberId();
        log.info("명함 리스트 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(businessCardService.getBusinessCardList(loginMemberId));
    }

    @DeleteMapping
    public ApiResponse<?> deleteBusinessCard(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("명함 삭제 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(businessCardService.delete(idx, loginMemberId));
    }

    @PatchMapping
    public ApiResponse<?> updateBusinessCard(
            @RequestParam(value = "idx") Long idx,
            @RequestBody BusinessCardRequestDto.UpdateBusinessCardRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        requestDto.setIdx(idx);
        log.info("명함 수정 요청 : loginMemberId = {}, idx = {}, 수정 내용 = {}", loginMemberId, idx, requestDto);
        return ApiResponse.onSuccess(businessCardService.update(requestDto, loginMemberId));
    }

    @GetMapping
    public ApiResponse<?> getBusinessCard(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("명함 조회 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(businessCardService.getBusinessCard(idx, loginMemberId));
    }



}
