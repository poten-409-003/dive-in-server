package com.poten.dive_in.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poten.dive_in.auth.dto.KakaoAccountDto;
import com.poten.dive_in.auth.dto.KakaoTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoService {

    @Value("${kakao.kakao_rest_api_key}")
    private String KAKAO_REST_API_KEY;

    @Value("${kakao.kakao_client_secret}")
    private String KAKAO_CLIENT_SECRET;

    public KakaoTokenDto getKakaoAccessToken(String code, String redirectUri) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_REST_API_KEY);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", KAKAO_CLIENT_SECRET);

        // 헤더와 바디 합치기 위해 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 카카오로부터 Access token 수신
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON parsing
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto;

    }

    public KakaoAccountDto getKakaoInfo(String kakaoAccessToken) {

        log.info("액세스토큰: " + kakaoAccessToken);

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 보내고, response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        log.info("카카오 서버에서 정상적으로 데이터를 수신했습니다.");

        // JSON Parsing
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoAccountDto kakaoAccountDto = null;
        try {
            JsonNode root = objectMapper.readTree(accountInfoResponse.getBody());

            JsonNode kakaoAccountNode = root.path("kakao_account");

            KakaoAccountDto.Profile profile = objectMapper.treeToValue(kakaoAccountNode.path("profile"), KakaoAccountDto.Profile.class);
            log.info("프로필: " + profile.getNickname());

            String email = kakaoAccountNode.path("email").asText();
            log.info("이메일: " + email);

            // DTO 생성
            kakaoAccountDto = KakaoAccountDto.builder()
                    .email(email)
                    .profile(profile)
                    .build();

        } catch (Exception e) {
            log.error("JSON 파싱에 실패하였습니다.");
            e.printStackTrace();
        }

        return kakaoAccountDto;


    }

}
