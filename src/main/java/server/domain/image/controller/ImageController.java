package server.domain.image.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import server.domain.image.service.ImageService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ApiResponse<?> uploadImage(@RequestPart(value="image", required = true) MultipartFile image) {
        String loginMemberId = getLoginMemberId();
        log.info("이미지 업로드 요청 : memberId = {}, image = {}", loginMemberId, image.getOriginalFilename());
        return ApiResponse.onSuccess(imageService.uploadImg(image,loginMemberId));

    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
