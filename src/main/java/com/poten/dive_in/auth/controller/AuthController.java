package com.poten.dive_in.auth.controller;

import com.poten.dive_in.auth.dto.KakaoAccountDto;
import com.poten.dive_in.auth.dto.KakaoTokenDto;
import com.poten.dive_in.auth.dto.UserInfoResponseDto;
import com.poten.dive_in.auth.service.AuthService;
import com.poten.dive_in.auth.service.KakaoService;
import com.poten.dive_in.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;

    @GetMapping("/login/kakao")
    public ResponseEntity<CommonResponse<Object>> login(@RequestParam String code, @RequestParam String redirect_uri){

        // 카카오 API에 액세스 토큰 요청
        KakaoTokenDto kakaoTokenDto = kakaoService.getKakaoAccessToken(code);

        // 액세스 토큰으로 유저 정보 요청
        KakaoAccountDto kakaoAccountDto = kakaoService.getKakaoInfo(kakaoTokenDto.getAccessToken());

        UserInfoResponseDto userInfoResponseDto = authService.login(kakaoAccountDto);

        return new ResponseEntity<>(CommonResponse.success(null,userInfoResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<CommonResponse<Object>> logout(HttpServletRequest request,HttpServletResponse response){
        authService.logOut(request,response);
        return new ResponseEntity<>(CommonResponse.success(null,null), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<Object>> getCurrentUserInfo(Principal principal){
        UserInfoResponseDto userInfoResponseDto = authService.getCurrentUserInfo(principal.getName());
        return new ResponseEntity<>(CommonResponse.success(null,userInfoResponseDto), HttpStatus.OK);
    }

}
