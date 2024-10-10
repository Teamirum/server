package server.domain.businessCard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.domain.businessCard.domain.BusinessCard;
import server.domain.businessCard.dto.BusinessCardRequestDto;
import server.domain.businessCard.dto.BusinessCardResponseDto;
import server.domain.businessCard.service.BusinessCardService;
import server.domain.businessCard.service.QRCodeService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

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
        return ApiResponse.onSuccess(businessCardService.upload(requestDto, loginMemberId));
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

        // 수정된 정보로 QR 코드 데이터를 JSON 형식으로 구조화
        String qrCodeData = String.format(
                "{\"name\":\"%s\", \"phone\":\"%s\", \"email\":\"%s\", \"position\":\"%s\", \"part\":\"%s\", \"company\":\"%s\", \"address\":\"%s\"}",
                updatedCard.getName(),
                updatedCard.getPhoneNum(),
                updatedCard.getEmail(),
                updatedCard.getPosition(),
                updatedCard.getPart(),
                updatedCard.getCompany(),
                updatedCard.getAddress()
        );

        try {
            // QR 코드 이미지 생성
            ByteArrayOutputStream outputStream = qrCodeService.generateQRCode(qrCodeData);

            // ByteArrayOutputStream을 Base64로 변환
            String qrCodeBase64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());

            return ApiResponse.onSuccess(qrCodeBase64);  // 업데이트된 QR 코드 반환
        } catch (Exception e) {
            log.error("QR 코드 생성 중 오류 발생", e);
            return ApiResponse.onFailure("QR_CODE_GENERATION_ERROR", "QR 코드 생성에 실패했습니다.", null);
        }
    }

    @GetMapping
    public ApiResponse<?> getBusinessCard(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("명함 조회 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(businessCardService.getBusinessCard(idx, loginMemberId));
    }

    //명함 등록시 QR 코드 생성
    @PostMapping("/qr-code")
    public ApiResponse<?> generateQRCode(@RequestBody BusinessCardRequestDto.UploadBusinessCardRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("QR 코드 생성 요청 : 현재 loginMemberId = {}, businessName = {}", loginMemberId, requestDto.getName());

        businessCardService.upload(requestDto, loginMemberId);


        // QR 코드 데이터를 JSON 형식으로 구조화
        String qrCodeData = String.format(
                "{\"name\":\"%s\", \"phone\":\"%s\", \"email\":\"%s\", \"position\":\"%s\", \"part\":\"%s\", \"company\":\"%s\", \"address\":\"%s\"}",
                requestDto.getName(),
                requestDto.getPhoneNum(),
                requestDto.getEmail(),
                requestDto.getPosition(),
                requestDto.getPart(),
                requestDto.getCompany(),
                requestDto.getAddress()
        );

        try {
            // QR 코드 이미지 생성
            ByteArrayOutputStream outputStream = qrCodeService.generateQRCode(qrCodeData);

            // ByteArrayOutputStream을 Base64로 변환
            String qrCodeBase64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());


            BusinessCard businessCard = businessCardService.findMemberIdx(loginMemberId);

            //MemberBusinessCard 테이블에 저장
            businessCardService.uploadMemberBusinessCard(businessCard);

            return ApiResponse.onSuccess(qrCodeBase64);
        } catch (Exception e) {
            log.error("QR 코드 생성 중 오류 발생", e);
            // 실패한 경우 응답 생성
            return ApiResponse.onFailure("QR_CODE_GENERATION_ERROR", "QR 코드 생성에 실패했습니다.", null);
        }
    }

    @PostMapping("/scanFriendQrCode")
    public ApiResponse<?> scanFriendBusinessCard(@RequestParam("businessCardIdx") Long businessCardIdx) {
        String loginMemberId = getLoginMemberId();
        log.info("친구 명함 추가 요청: businessCardIdx = {}, loginMemberId = {}", businessCardIdx, loginMemberId);

        // 1. QR 코드로 명함 정보 가져오기
        BusinessCard businessCard = businessCardService.getBusinessCard(businessCardIdx, loginMemberId);

        // 2. 해당 명함을 MemberBusinessCard에 저장 (상태는 NOT_OWNER)
        businessCardService.addFriendBusinessCard(businessCardIdx, loginMemberId);

        return ApiResponse.onSuccess(businessCard);
    }

    //모든 친구 명함 조회
    @GetMapping("/friends/all")
    public ApiResponse<?> getFriendBusinessCardList() {
        String loginMemberId = getLoginMemberId();
        log.info("친구 명함 목록 조회 요청: loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(businessCardService.getAllFriendBusinessCards(loginMemberId));
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


}
