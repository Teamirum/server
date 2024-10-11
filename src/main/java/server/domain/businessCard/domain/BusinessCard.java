package server.domain.businessCard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessCard {

    private Long  idx;

    private Long memberIdx;      // 외래키, Member 테이블의 idx를 참조

    private String name;         // 이름

    private String company;      // 회사 이름

    private String phoneNum;     // 전화번호

    private String email;        // 이메일

    private String telNum;       // 회사 전화번호

    private String part;         // 부서

    private String position;     // 직위

    private String address;      // 주소

    private String imgUrl;

    public String getQrData() {
        return new String(String.format(
                "{\"idx\":\"%s\"}", this.idx
        ).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

}
