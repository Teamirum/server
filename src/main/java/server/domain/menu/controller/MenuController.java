package server.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.menu.dto.MenuRequestDto;
import server.domain.menu.service.MenuService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/register")
    public ApiResponse<?> registerMenu(@RequestBody MenuRequestDto.CreateMenuRequestDto requestDto) {
        String memberId = getLoginMemberId();
        log.info("메뉴 등록 요청 : memberId = {}", memberId);
        return ApiResponse.onSuccess(menuService.registerMenu(requestDto, memberId));
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllMenu() {
        String memberId = getLoginMemberId();
        log.info("메뉴 전체 조회 요청 : memberId = {}", memberId);
        return ApiResponse.onSuccess(menuService.getAllMenu(memberId));
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
