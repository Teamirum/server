//package server.global.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//@RequiredArgsConstructor
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    private final StompHandler stompHandler;
//    private final StompErrorHandler stompErrorHandler;
//
//    // sockJS Fallback을 이용해 노출할 endpoint 설정
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // 웹소켓이 handshake를 하기 위해 연결하는 endpoint
//        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns("*");
////                .withSockJS();
//        registry.setErrorHandler(stompErrorHandler);
//
//    }
//
//    //메세지 브로커에 관한 설정
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        // 서버 -> 클라이언트로 발행하는 메세지에 대한 endpoint 설정 : 구독
//        registry.enableSimpleBroker("/sub");
//
//        // 클라이언트->서버로 발행하는 메세지에 대한 endpoint 설정 : 구독에 대한 메세지
//        registry.setApplicationDestinationPrefixes("/pub");
//    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
//    }
//
//}