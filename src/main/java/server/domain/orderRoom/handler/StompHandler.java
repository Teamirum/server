package server.domain.orderRoom.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.domain.orderRoom.repository.RedisRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.auth.security.domain.CustomUserDetails;
import server.global.auth.security.service.JwtService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final RedisRepository redisRepository;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();//5

    // WebSocket을 통해 들어온 요청이 처리 되기 전에 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("StompHandler - preSend called. Command: {}", accessor.getCommand());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // CONNECT 메시지 처리
            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            String authHeader = null;
            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                authHeader = authorizationHeaders.get(0).trim();
            }

            Optional<String> accessToken = Optional.ofNullable(authHeader)
                    .filter(token -> token.startsWith("Bearer "))
                    .map(token -> token.replace("Bearer ", ""));

            log.info("Stomp Handler : accessToken : {}", accessToken);

            if (accessToken.isEmpty()) {
                log.error("Stomp Handler : 유효하지 않은 토큰입니다.");
                throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
            }

            String memberId = jwtService.extractMemberId(accessToken.get()).orElse(null);

            if (!jwtService.isTokenValid(accessToken.get())) {
                log.error("Stomp Handler : 유효하지 않은 토큰입니다. memberId : {}", memberId);
                throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
            }

            Member member = memberRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
            saveAuthentication(member);
            // 사용자 정보를 세션에 저장
            accessor.setUser(new Principal() {
                @Override
                public String getName() {
                    return memberId;
                }
            });

            log.info("Stomp Handler : CONNECTED. memberId : {}", memberId);

        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) { // 채팅룸 구독요청(진입) -> CrewMember인지 검증
            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            String authHeader = null;
            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                authHeader = authorizationHeaders.get(0).trim();
            }

            log.info("Stomp Handler SUBSCRIBE : authHeader : {}", authHeader);

            Optional<String> accessToken = Optional.ofNullable(authHeader)
                    .filter(token -> token.startsWith("Bearer "))
                    .map(token -> token.replace("Bearer ", ""));

            log.info("Stomp Handler SUBSCRIBE : accessToken : {}", accessToken);

            if (accessToken.isEmpty()) {
                log.error("Stomp Handler : 유효하지 않은 토큰입니다.");
                throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
            }

            String memberId = jwtService.extractMemberId(accessToken.get()).orElse(null);

            if (!jwtService.isTokenValid(accessToken.get())) {
                log.error("Stomp Handler : 유효하지 않은 토큰입니다. memberId : {}", memberId);
                throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
            }

            Member member = memberRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
            saveAuthentication(member);
            // 사용자 정보를 세션에 저장
            accessor.setUser(new Principal() {
                @Override
                public String getName() {
                    return memberId;
                }
            });

        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            // DISCONNECT 메시지 처리
            Principal principal = accessor.getUser();
            if (principal != null) {
                String memberId = principal.getName();
                Member member = memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

                // 함께 주문에서 나가는 로직
                if (redisRepository.existMyInfo(member.getIdx())) {
                    Long orderIdx = redisRepository.getMemberEnteredOrderRoomIdx(member.getIdx());
                    log.info("Exit orderRoom. memberIdx : {}, orderRoomIdx : {}", member.getIdx(), orderIdx);
                    if (orderIdx == null) {
                        log.error("Stomp Handler : 주문방을 찾는데 실패하였습니다. memberIdx : {}", member.getIdx());
                        throw new MessageDeliveryException(ErrorStatus.ORDER_ROOM_NOT_FOUND.getMessage());
                    }

                    // 채팅방 퇴장 정보 저장
                    if (redisRepository.existMemberInOrderRoom(orderIdx, member.getIdx())) {
                        redisRepository.exitMemberEnterOrderRoom(member.getIdx());
                    }

                    redisRepository.minusMemberCnt(orderIdx, member.getIdx());
                }
            } else {
                log.error("Stomp Handler : 사용자 정보를 찾을 수 없습니다.");
            }
        } else {
            // 기타 명령에 대한 처리 (필요에 따라 추가)
        }

        return message;
    }

    private void saveAuthentication(Member member) {
        CustomUserDetails userDetails = CustomUserDetails.create(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));


        SecurityContext context = SecurityContextHolder.createEmptyContext();//5
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        log.info("Authentication success: memberId = {}", member.getMemberId());
    }
}