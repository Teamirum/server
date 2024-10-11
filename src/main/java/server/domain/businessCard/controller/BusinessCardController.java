package server.domain.businessCard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.domain.businessCard.domain.BusinessCard;
import server.domain.businessCard.dto.BusinessCardRequestDto;
import server.domain.businessCard.dto.BusinessCardResponseDto;
import server.domain.businessCard.dto.MemberBusinessCardRequestDto;
import server.domain.businessCard.service.BusinessCardService;
import server.domain.businessCard.service.QRCodeService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/businessCard")
@Slf4j
@RequiredArgsConstructor
public class BusinessCardController {

    private final BusinessCardService businessCardService;
    private final QRCodeService qrCodeService; // QRCodeService 주입


    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @PostMapping
    public ApiResponse<?> uploadBusinessCard(@RequestBody BusinessCardRequestDto.UploadBusinessCardRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("명함 업로드 요청 : 현재loginMemberId = {}, businessName = {}", loginMemberId ,requestDto.name);
        businessCardService.upload(requestDto, loginMemberId);

        BusinessCard businessCard = businessCardService.findMemberIdx(loginMemberId);

        //MemberBusinessCard 테이블에 저장
        businessCardService.uploadMemberBusinessCard(businessCard);

        return ApiResponse.onSuccess(businessCard);
    }

    // 내 명함 조회
    @GetMapping("/myBusinessCard")
    public ApiResponse<?> getBusinessCardList() {
        String loginMemberId = getLoginMemberId();
        log.info("명함 리스트 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(businessCardService.getBusinessCardList(loginMemberId));
    }

    @DeleteMapping
    public ApiResponse<?> deleteBusinessCard() {
        String loginMemberId = getLoginMemberId();
        log.info("명함 삭제 요청 : loginMemberId = {}, idx = {}", loginMemberId);
        return ApiResponse.onSuccess(businessCardService.delete(loginMemberId));
    }

    @PatchMapping
    public ApiResponse<?> updateBusinessCard(
            @RequestParam(value = "idx") Long idx,
            @RequestBody BusinessCardRequestDto.UpdateBusinessCardRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        requestDto.setIdx(idx);
        log.info("명함 수정 요청 : loginMemberId = {}, idx = {}, 수정 내용 = {}", loginMemberId, idx, requestDto);

        // 명함 정보 업데이트
        businessCardService.update(requestDto, loginMemberId);

        // 수정된 명함 정보를 가져옴
        BusinessCard updatedCard = businessCardService.getBusinessCard(idx, loginMemberId);

        return ApiResponse.onSuccess(updatedCard);
    }


    @GetMapping
    public ApiResponse<?> getBusinessCard(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("명함 조회 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(businessCardService.getBusinessCard(idx, loginMemberId));
    }


    @PostMapping("/scanFriendQrCode")
    public ApiResponse<?> scanFriendBusinessCard(@RequestParam("businessCardIdx") Long businessCardIdx) {
        String loginMemberId = getLoginMemberId();
        log.info("친구 명함 추가 요청: businessCardIdx = {}, loginMemberId = {}", businessCardIdx, loginMemberId);

        BusinessCard businessCard = businessCardService.getBusinessCard(businessCardIdx, loginMemberId);

        businessCardService.addFriendBusinessCard(businessCardIdx, loginMemberId);

        return ApiResponse.onSuccess(businessCard);
    }

    //모든 친구 명함 조회
    @GetMapping("/friends/all")
    public ApiResponse<?> getFriendBusinessCardList() {
        String loginMemberId = getLoginMemberId();
        log.info("친구 명함 목록 조회 요청: loginMemberId = {}", loginMemberId);
        // 모든 비즈니스 카드 목록을 가져오기
        BusinessCardResponseDto.BusinessCardListResponseDto businessCards = businessCardService.getAllFriendBusinessCards(loginMemberId);
        return ApiResponse.onSuccess(businessCards); // 모든 비즈니스 카드 목록 반환
    }

    // 친구 명함 삭제
    @DeleteMapping("/friends")
    public ApiResponse<?> deleteFriendBusinessCard(@RequestParam("businessCardIdx") Long businessCardIdx) {
        String loginMemberId = getLoginMemberId();
        log.info("친구 명함 삭제 요청: BusinessCardIdx = {}, loginMemberId = {}", businessCardIdx, loginMemberId);
        return ApiResponse.onSuccess( businessCardService.deleteFriendBusinessCard(businessCardIdx, loginMemberId));
    }

    // 특정 친구 명함 조회
    @GetMapping("/friends")
    public ApiResponse<?> getFriendBusinessCard(@RequestParam("businessCardIdx") Long businessCardIdx) {
        String loginMemberId = getLoginMemberId();
        log.info("친구 명함 조회 요청: BusinessCardIdx = {}, loginMemberId = {}", businessCardIdx, loginMemberId);
        return ApiResponse.onSuccess(businessCardService.getFriendBusinessCard(businessCardIdx, loginMemberId));
    }

    // 친구 명함 수정
    @PatchMapping("/friends")
    public ApiResponse<?> updateFriendBusinessCard(
            @RequestParam("businessCardIdx") Long businessCardIdx,
            @RequestBody MemberBusinessCardRequestDto.UpdateMemberBusinessCardRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("친구 명함 수정 요청: BusinessCardIdx = {}, loginMemberId = {}, 수정 내용 = {}", businessCardIdx, loginMemberId, requestDto);
        return ApiResponse.onSuccess(businessCardService.updateFriendBusinessCard(businessCardIdx, requestDto, loginMemberId));
    }


}
