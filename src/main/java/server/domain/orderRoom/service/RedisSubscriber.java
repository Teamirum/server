package server.domain.orderRoom.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import static server.domain.orderRoom.dto.OrderRoomResponseDto.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행되면 대기하고 있던 onMessage가 메시지를 받아 messagingTemplate를 이용하여 websocket 클라이언트들에게 메시지 전달
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아 역직렬화
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            JsonNode jsonNode = objectMapper.readTree(publishMessage);
            String messageType = jsonNode.get("type").asText();
            if (messageType.equals("ENTER")) {
                EnterOrderRoomResponseDto enterOrderRoomResponseDto = objectMapper.treeToValue(jsonNode, EnterOrderRoomResponseDto.class);
                messagingTemplate.convertAndSend("/sub/order/room/" + enterOrderRoomResponseDto.getOrderIdx(), enterOrderRoomResponseDto);
            }

            // Websocket 구독자에게 채팅 메시지 전송


        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
        }
    }
}
