package server.domain.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.domain.message.dto.MessageDtoConverter;
import server.domain.message.dto.MessageResponseDto;
import server.global.security.service.JwtService;
import server.global.util.RedisUtil;

import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService implements InitializingBean {

    @Value("${coolsms.api_key}")
    private String apiKey;

    @Value("${coolsms.api_secret}")
    private String apiSecret;

    @Value("${coolsms.from}")
    private String fromNumber;


    private final RedisUtil redisUtil;
    private final JwtService jwtService;
    private DefaultMessageService defaultMessageService;

    private static final String domain = "https://api.coolsms.co.kr";
    private static final int CODE_LENGTH = 6;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.defaultMessageService = new DefaultMessageService(apiKey, apiSecret, domain);
    }


    public MessageResponseDto.SendMessageResponseDto sendMessage(String phoneNum) {

        Message message = new Message();
        String randomNumber = makeRandomNumber();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(fromNumber);
        message.setTo(phoneNum);
        message.setText("[모두의 결제] 인증번호는 " + randomNumber + " 입니다. 타인에게 노출하지 마세요.");

        SingleMessageSentResponse response = defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        redisUtil.setDataExpire(phoneNum, randomNumber, 60*5L);
        return MessageResponseDto.SendMessageResponseDto.builder()
                .isSuccess(true)
                .build();
    }

    public MessageResponseDto.CheckMessageSuccessResponseDto checkMessage(String phoneNum, String authNum) {
        String data = redisUtil.getData(phoneNum);
        if (data == null || !data.equals(authNum)) {
            log.info("인증에 실패하였습니다. phoneNum = {}", phoneNum);
            return MessageDtoConverter.convertToCheckMessageSuccessResponseDto(true, null);
        }
        redisUtil.deleteData(phoneNum);
        String phoneAuthToken = jwtService.createPhoneAuthToken(phoneNum);
        log.info("인증에 성공하였습니다. phoneNum = {}", phoneNum);
        return MessageDtoConverter.convertToCheckMessageSuccessResponseDto(true, phoneAuthToken);
    }

    //임의의 6자리 양수를 반환합니다.
    private String makeRandomNumber() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for(int i = 0; i < CODE_LENGTH; i++) {
            randomNumber.append(r.nextInt(10));
        }

        return randomNumber.toString();
    }
}
