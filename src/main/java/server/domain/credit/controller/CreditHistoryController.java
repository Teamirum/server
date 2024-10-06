package server.domain.credit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.credit.dto.CreditHistoryRequestDto;
import server.domain.credit.service.CreditHistoryService;
import server.domain.credit.service.CreditService;
import server.domain.member.service.MemberService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/credit/history")
@Slf4j
@RequiredArgsConstructor
public class CreditHistoryController {

    private final CreditHistoryService creditHistoryService;
    private final CreditService creditService;
    private  final MemberService memberService;



}
