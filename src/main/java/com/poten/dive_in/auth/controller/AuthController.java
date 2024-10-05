package com.poten.dive_in.auth.controller;

import com.poten.dive_in.auth.dto.KakaoAccountDto;
import com.poten.dive_in.auth.dto.KakaoTokenDto;
import com.poten.dive_in.auth.dto.LoginResponseDto;
import com.poten.dive_in.auth.dto.UserProfileDto;

import com.poten.dive_in.auth.service.AuthService;
import com.poten.dive_in.auth.service.KakaoService;
import com.poten.dive_in.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;

    @GetMapping("/login/kakao")
    public ResponseEntity<CommonResponse<Object>> login(@RequestParam String code, @RequestParam("redirect_uri") String redirectUri, HttpServletResponse response){

        // 카카오 API에 액세스 토큰 요청
        KakaoTokenDto kakaoTokenDto = kakaoService.getKakaoAccessToken(code,redirectUri);

        // 액세스 토큰으로 유저 정보 요청
        KakaoAccountDto kakaoAccountDto = kakaoService.getKakaoInfo(kakaoTokenDto.getAccessToken());

        LoginResponseDto loginResponseDto = authService.login(kakaoAccountDto,response);

        return new ResponseEntity<>(CommonResponse.success(null,loginResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<CommonResponse<Object>> logout(HttpServletRequest request,HttpServletResponse response){
        authService.logOut(request,response);
        return new ResponseEntity<>(CommonResponse.success(null,null), HttpStatus.OK);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<CommonResponse<Object>> getUserProfile(Principal principal){
        UserProfileDto userProfileDto = authService.getUserProfile(principal.getName());
        return new ResponseEntity<>(CommonResponse.success(null, userProfileDto), HttpStatus.OK);
    }

    @PutMapping("/user/profile")
    public ResponseEntity<CommonResponse<Object>> updateUserProfile(Principal principal, @RequestParam("nickname") String nickname, @RequestParam(value = "profileImage",required = false) MultipartFile file){
        UserProfileDto updatedUserProfileDto = authService.updateUserProfile(principal.getName(),nickname,file);
        return new ResponseEntity<>(CommonResponse.success(null, updatedUserProfileDto), HttpStatus.OK);
    }

    @DeleteMapping("/user/profile")
    public ResponseEntity<CommonResponse<Object>> deleteUserProfile(Principal principal,HttpServletRequest request,  HttpServletResponse response){
        authService.deleteUser(principal.getName(), request, response);
        return new ResponseEntity<>(CommonResponse.success("회원 탈퇴가 완료되었습니다.",null), HttpStatus.OK);
    }

}
