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
        String baseUrl = "http://localhost:5173/friend-card-registration"; // 기본 URL
        String queryParam = "?idx=" + this.idx; // idx를 쿼리 파라미터로 추가
        String fullUrl = baseUrl + queryParam; // 완전한 URL 생성

        return new String(String.format(
                "{\"url\":\"%s\"}", fullUrl
        ).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

}
