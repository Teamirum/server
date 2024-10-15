package server.domain.orderRoom.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import static server.domain.orderRoom.dto.OrderRoomResponseDto.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final GenericJackson2JsonRedisSerializer serializer;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행되면 이 메서드가 호출되어 WebSocket 클라이언트들에게 메시지를 전달합니다.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // GenericJackson2JsonRedisSerializer를 사용하여 메시지를 역직렬화
            Object obj = serializer.deserialize(message.getBody());

            if (obj instanceof EnterOrderRoomResponseDto) {
                EnterOrderRoomResponseDto dto = (EnterOrderRoomResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received ENTER message: {}", dto);
            } else if (obj instanceof OrderRoomMenuInfoListDto) {
                OrderRoomMenuInfoListDto dto = (OrderRoomMenuInfoListDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received MENU_INFO message: {}", dto);
            } else if (obj instanceof OrderRoomMenuSelectionResponseDto) {
                OrderRoomMenuSelectionResponseDto dto = (OrderRoomMenuSelectionResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received MENU_SELECT or MENU_CANCEL message: {}", dto);
            } else if (obj instanceof OrderRoomReadyToPayResponseDto) {
                OrderRoomReadyToPayResponseDto dto = (OrderRoomReadyToPayResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received READY_TO_PAY or CANCEL_READY_TO_PAY message: {}", dto);
            } else if (obj instanceof StartPayResponseDto) {
                StartPayResponseDto dto = (StartPayResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received START_PAY message: {}", dto);
            } else if (obj instanceof OrderRoomInfoResponseDto) {
                OrderRoomInfoResponseDto dto = (OrderRoomInfoResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received PARTICIPANT_INFO message: {}", dto);
            } else if (obj instanceof OrderRoomPriceSelectionResponseDto) {
                OrderRoomPriceSelectionResponseDto dto = (OrderRoomPriceSelectionResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received PRICE_SELECT message: {}", dto);
            } else if (obj instanceof OrderRoomMemberInfoListResponseDto) {
                OrderRoomMemberInfoListResponseDto dto = (OrderRoomMemberInfoListResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received MEMBER_LIST_INFO message: {}", dto);
            } else if (obj instanceof OrderRoomGameResultResponseDto) {
                OrderRoomGameResultResponseDto dto = (OrderRoomGameResultResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received GAME_RESULT message: {}", dto);
            } else if (obj instanceof IsGameRoomResponseDto) {
                IsGameRoomResponseDto dto = (IsGameRoomResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.info("Received IS_GAME message: {}", dto);
            } else if (obj instanceof ErrorResponseDto) {
                ErrorResponseDto dto = (ErrorResponseDto) obj;
                messagingTemplate.convertAndSend("/sub/order/room/" + dto.getOrderIdx(), dto);
                log.error("Received START_PAY message: {}", dto);
            }
            else {
                log.error("RedisSubscriber: 알 수 없는 메시지 타입입니다. 클래스 = {}", obj.getClass());
            }

        } catch (Exception e) {
            log.error("RedisSubscriber: Exception occurred while processing message - {}", e.getMessage(), e);
        }
    }
}