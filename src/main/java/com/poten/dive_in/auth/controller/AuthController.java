package com.poten.dive_in.auth.controller;

import com.poten.dive_in.auth.service.AuthService;
import com.poten.dive_in.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/login")
    public ResponseEntity<CommonResponse<Object>> login(@RequestParam String email,@RequestParam String nickName, HttpServletResponse response){

        authService.login(email,nickName, response);
        return new ResponseEntity<>(CommonResponse.success(null,null), HttpStatus.OK);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<CommonResponse<Object>> logout(HttpServletRequest request,HttpServletResponse response){
        authService.logOut(request,response);
        return new ResponseEntity<>(CommonResponse.success(null,null), HttpStatus.OK);
    }

}
