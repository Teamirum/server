package server.global.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.global.apiPayload.code.BaseErrorCode;
import server.global.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근 권한이 없는 요청입니다."),
    _LOGIN_FAILURE(HttpStatus.NOT_FOUND, "COMMON404", "요청 리소스를 찾을 수 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _SERVICE_UNAVAILABLE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "COMMON503", "서버가 일시적으로 사용중지 되었습니다."),
    _JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "JSON 처리 중 오류가 발생하였습니다."),

    // 동시성 제어
    FAILED_TO_ACQUIRE_LOCK(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5001", "Lock 획득에 실패하였습니다."),

    //유저 응답
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "유저가 존재하지 않습니다."),
    MEMBER_EMAIL_PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "MEMBER4002", "이메일 또는 비밀번호가 일치하지 않습니다."),
    MEMBER_PHONE_NUM_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4003", "중복된 휴대폰 번호입니다."),
    MEMBER_NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4004", "중복된 닉네임입니다."),
    MEMBER_PHONE_AUTH_NOT_VALID(HttpStatus.UNAUTHORIZED, "MEMBER4005", "유효하지 않은 전화번호 인증 토큰입니다"),
    MEMBER_PHONE_TOKEN_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "MEMBER4006", "전화번호 인증 토큰이 제공되지 않았습니다."),
    MEMBER_LOGIN_NOT_SUPPORT(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "MEMBER4007", "지원되지 않는 로그인 형식입니다."),
    MEMBER_TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "MEMBER4008", "유효하지 않은 토큰입니다."),
    MEMBER_SOCIAL_TOKEN_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "MEMBER4009", "소셜 토큰이 제공되지 않았습니다."),
    MEMBER_NOT_AUTHENTICATED(HttpStatus.FORBIDDEN, "MEMBER4010", "권한이 없는 사용자입니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4011", "이미 존재하는 회원입니다."),
    MEMBER_AUTHORIZATION_NOT_VALID(HttpStatus.UNAUTHORIZED, "MEMBER4012", "유효하지 않은 인증정보 입니다."),
    MEMBER_PHONE_CONFIRM_BAD_REQUEST(HttpStatus.BAD_REQUEST, "MEMBER4013", "문자 인증 요청이 잘못되었습니다."),
    MEMBER_ALREADY_CONNECTED(HttpStatus.BAD_REQUEST, "MEMBER4014", "이미 마이데이터가 연동된 계정입니다."),

    //권한
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "권한이 없습니다."),

    // 가맹점 응답
    MARKET_NOT_FOUND(HttpStatus.BAD_REQUEST, "MARKET4001", "해당 가맹점이 존재하지 않습니다."),
    MARKET_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MARKET4002", "이미 가맹점을 보유한 회원입니다"),

    // 계좌 응답
    ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "ACCOUNT4001", "해당 계좌가 존재하지 않습니다."),
    ACCOUNT_DUPLICATE(HttpStatus.BAD_REQUEST, "ACCOUNT4002", "이미 등록된 계좌입니다."),

    ACCOUNT_NOT_ENOUGH_AMOUNT(HttpStatus.BAD_REQUEST, "ACCOUNT4003", "계좌 잔액이 부족합니다."),
    ACCOUNT_HISTORY_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "ACCOUNT4004", "유효하지 않은 계좌 히스토리 타입입니다."),
    ACCOUNT_HISTORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "ACCOUNT4005", "해당 계좌 히스토리가 존재하지 않습니다."),

    ACCOUNT_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "ACCOUNT5001", "계좌 저장에 실패하였습니다."),

    // 카드 응답
    CREDIT_CARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "CREDIT4001", "해당 카드가 존재하지 않습니다."),
    CREDIT_CARD_DUPLICATE(HttpStatus.BAD_REQUEST, "CARD4002", "이미 등록된 카드입니다."),

    CREDIT_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "CREDIT400", "카드 결제 권한이 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "CREDIT400, UNAUTHORIZED", "카드 권한이 없습니다."),
    CREDIT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CREDIT4001", "해당 카드가 존재하지 않습니다."),
    CREDIT_CARD_EXPIRED(HttpStatus.BAD_REQUEST, "CARD4003", "카드가 만료되었습니다."),
    CREDIT_HISTORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CREDIT4004", "해당 카드 히스토리가 존재하지 않습니다."),


    CREDIT_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "CREDIT5001", "카드 저장에 실패하였습니다."),
    CREDIT_EXPIRATION_DATE_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "CREDIT5002", "유효기간이 잘못되었습니다."),



    // 주문 응답
    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER4001", "해당 주문이 존재하지 않습니다."),

    // 명함 응답
    BUSINESS_CARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "BUSINESSCARD4001", "해당 명함이 존재하지 않습니다."),
    BUSINESS_CARD_DUPLICATE(HttpStatus.BAD_REQUEST, "BUSINESSCARD4002", "이미 등록된 명함입니다."),


    BUSINESS_CARD_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "BUSINESSCARD5001", "명함 저장에 실패하였습니다."),
    BUSINESS_CARD_UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "BUSINESSCARD5002", "명함 수정에 실패하였습니다."),
    BUSINESS_CARD_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "BUSINESSCARD5003", "명함 삭제에 실패하였습니다."),
    BUSINESS_CARD_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "BUSINESSCARD4003", "명함 권한이 없습니다."),
    BUSINESS_FRIEND_CARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "BUSINESSCARD4004", "해당 친구명함이 존재하지 않습니다."),

    // 거래 응답
    TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "TRANSACTION4001", "해당 거래가 존재하지 않습니다."),
    TRANSACTION_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "TRANSACTION4002", "거래 권한이 없습니다."),
    TRANSACTION_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "TRANSACTION5001", "거래 저장에 실패하였습니다."),

    ORDER_MENU_CNT_ERROR(HttpStatus.BAD_REQUEST, "ORDER4002", "주문은 0개 이상 해야합니다."),
    ORDER_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER4003", "해당 함께결제가 존재하지 않습니다."),
    ORDER_ROOM_MEMBER_CNT_CANNOT_BE_MINUS(HttpStatus.BAD_REQUEST, "ORDER4004", "함께결제 인원은 0명 이상이어야 합니다."),
    ORDER_ROOM_MEMBER_CNT_CANNOT_EXCEED(HttpStatus.BAD_REQUEST, "ORDER4005", "함께결제 제한 인원수를 초과할 수 없습니다."),
    ORDER_ROOM_MEMBER_CNT_NOT_MATCH(HttpStatus.BAD_REQUEST, "ORDER4006", "함께결제 인원수가 일치하지 않습니다."),
    ORDER_ROOM_MEMBER_CNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "ORDER4007", "함께결제 인원수가 부족합니다."),
    ORDER_ROOM_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER4008", "해당 함께결제 참여자가 존재하지 않습니다."),
    ORDER_ROOM_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "ORDER4009", "이미 함께결제가 진행중인 주문이 있습니다."),
    ORDER_MENU_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER4010", "주문한 메뉴가 존재하지 않습니다."),
    ORDER_MEMBER_PARTICIPANT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER4011", "참여중인 주문방이 존재하지 않습니다."),
    ORDER_MEMBER_ALREADY_IN_OTHER_ROOM(HttpStatus.BAD_REQUEST, "ORDER4012", "이미 다른 주문방에 참여중입니다."),
    ORDER_MEMBER_ALREADY_IN_ROOM(HttpStatus.BAD_REQUEST, "ORDER4013", "이미 참여중인 주문방 입니다."),
    ORDER_MENU_MEMBER_CNT_ERROR(HttpStatus.BAD_REQUEST, "ORDER4014", "메뉴 주문 인원수가 잘못되었습니다."),
    ORDER_ROOM_CHANNEL_TOPIC_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER4015", "주문방 채널이 존재하지 않습니다."),
    ORDER_ROOM_PAY_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "ORDER4016", "이미 결제가 진행중인 주문방입니다."),
    ORDER_ROOM_PRICE_NOT_MATCH(HttpStatus.BAD_REQUEST, "ORDER4017", "함께 결제 금액이 일치하지 않습니다."),
    ORDER_ROOM_PRICE_NOT_VALID(HttpStatus.BAD_REQUEST, "ORDER4018", "유효하지 않은 결제 금액입니다."),

    ORDER_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "ORDER5001", "주문 저장에 실패하였습니다."),
    ORDER_MENU_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "ORDER5002", "메뉴 저장에 실패하였습니다."),
    //결제 응답
    PAY_METHOD_NOT_FOUND(HttpStatus.BAD_REQUEST, "PAY4001", "올바르지 않은 결제 방식입니다."),
    PAY_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "PAY4002", "올바르지 않은 결제 타입입니다."),
    PAY_ALREADY_COMPLETE(HttpStatus.BAD_REQUEST, "PAY4003", "이미 결제가 완료된 주문입니다."),

    PAY_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "PAY5001", "결제 저장에 실패하였습니다."),

    // 메뉴 응답
    MENU_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MENU4001", "이미 등록된 메뉴입니다."),
    MENU_NOT_FOUND(HttpStatus.BAD_REQUEST, "MENU4002", "해당 메뉴가 존재하지 않습니다."),

    MENU_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MENU5001", "메뉴 저장에 실패하였습니다."),


    //사진 응답
    IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "IMG4001", "사진 업로드에 실패하였습니다."),
    IMAGE_DOWNLOAD_FAIL(HttpStatus.NOT_FOUND, "IMG4002", "사진 다운로드에 실패하였습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMG4003", "사진 조회에 실패하였습니다."),
    IMAGE_DELETE_FAIL(HttpStatus.BAD_REQUEST, "IMG4004", "사진 삭제에 실패하였습니다."),
    IMAGE_UPLOAD_TIME_COUNT_LIMIT(HttpStatus.BAD_REQUEST, "IMG4005", "사진 업로드 제한 횟수를 초과하였습니다."),

    // 신고 응답
    REPORT_NOT_FOUND(HttpStatus.BAD_REQUEST, "REPORT4001", "신고내역이 존재하지 않습니다."),
    REPORT_TITLE_NULL(HttpStatus.BAD_REQUEST, "REPORT4002", "신고제목을 작성해주세요."),
    REPORT_CONTENT_NULL(HttpStatus.BAD_REQUEST, "REPORT4003", "신고내용을 작성해주세요."),

    // 소셜 로그인 응답
    SOCIAL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SOCIAL4001", "소셜 인증에 실패하였습니다."),
    KAKAO_SOCIAL_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "SOCIAL4002", "KAKAO 소셜 정보를 불러오는데에 실패하였습니다."),
    NAVER_SOCIAL_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "SOCIAL4003", "NAVER 소셜 정보를 불러오는데에 실패하였습니다."),
    GOOGLE_SOCIAL_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "SOCIAL4004", "GOOGLE 소셜 정보를 불러오는데에 실패하였습니다."),
    KAKAO_SOCIAL_UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SOCIAL5001", "KAKAO 소셜 연동 해제에 실패하였습니다."),
    NAVER_SOCIAL_UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SOCIAL5002", "NAVER 소셜 연동 해제에 실패하였습니다."),
    GOOGLE_SOCIAL_UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SOCIAL5003", "GOOGLE 소셜 연동 해제에 실패하였습니다."),
    // FCM 응답
    FCM_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "FCM4001", "FCM 토큰이 존재하지 않습니다."),
    FCM_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "FCM4002", "FCM 권한이 없습니다."),

    // 시간 제한
    TIME_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,"TIME4001", "시간 제한"),

    //푸시알림 응답
    NOTIFY_SEND_FAIL(HttpStatus.BAD_REQUEST,"NOTIFY4001", "알림 보내기를 실패하였습니다."),
    NOTIFY_UNSUPPORTED_TYPE(HttpStatus.BAD_REQUEST,"NOTIFY4002", "지원하지 않는 알림 타입입니다."),
    NOTIFY_NOT_FOUND(HttpStatus.BAD_REQUEST,"NOTIFY4003", "알림이 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .httpStatus(httpStatus)
            .build();
    }
}