package com.poten.dive_in.auth.service;

import com.poten.dive_in.auth.dto.KakaoAccountDto;
import com.poten.dive_in.auth.dto.LoginResponseDto;
import com.poten.dive_in.auth.dto.UserProfileDto;
import com.poten.dive_in.auth.enums.SocialType;
import com.poten.dive_in.auth.jwt.JwtTokenProvider;
import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.entity.TokenManager;
import com.poten.dive_in.auth.enums.Role;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.auth.repository.TokenManagerRepository;
import com.poten.dive_in.common.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.poten.dive_in.common.service.S3Service.extractFileName;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenManagerRepository tokenManagerRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("사용자가 존재하지 않습니다."));
        return UserProfileDto.ofEntity(member);
    }

    @Transactional
    public UserProfileDto updateUserProfile(String email, String nickname, MultipartFile file) {

        // 해당 email의 member가 존재하는지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));

        // 이미지 수정이 있는지 확인
        String profileImageUrl;
        if (file != null && !file.isEmpty()) {
            profileImageUrl = member.getProfileImageUrl();

            // 기존 이미지가 있는 경우
            if (profileImageUrl != null) {
                // 카카오 이미지가 아닌 경우 S3에서 삭제
                if (!profileImageUrl.startsWith("http://k.kakaocdn.net")) {
                    String fileName = extractFileName(profileImageUrl);
                    s3Service.deleteFile(fileName);
                }
            }

            // 새로운 이미지 저장
            List<String> urlList = s3Service.uploadFile(Collections.singletonList(file));
            profileImageUrl = urlList.get(0);
        } else {
            profileImageUrl = member.getProfileImageUrl();
        }

        member.updateMember(nickname, profileImageUrl);
        return UserProfileDto.ofEntity(member);
    }


    @Transactional
    public LoginResponseDto login(KakaoAccountDto kakaoAccountDto,HttpServletResponse response){

        String email = kakaoAccountDto.getEmail();

        String nickname = kakaoAccountDto.getProfile().getNickname();

        String profileImageUrl = kakaoAccountDto.getProfile().getProfileImageUrl();

        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email).orElseGet(() -> {

            // 이메일이 없을 경우 새로운 Member 생성
            Member newMember = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .role(Role.ROLE_USER) // 디폴트 USER
                    .socialType(SocialType.KAKAO)
                    .profileImageUrl(profileImageUrl)
                    .build();
            return memberRepository.save(newMember);
        });

        // AccessToken 및 Refresh Token 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(),member.getEmail(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(),member.getEmail(),member.getRole());

        // 응답 헤더에 Access Token 및 Refresh Token 추가
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("X-Refresh-Token", refreshToken);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder().accessToken(accessToken)
                .refreshToken(refreshToken).build();


        // refreshToken 만료 기한
        LocalDateTime refreshTokenExpirationDate = jwtTokenProvider.getExpirationFromToken(refreshToken);

        // TokenManager 빌더 사용하여 생성
        TokenManager tokenManager = TokenManager.builder()
                .expirationDate(refreshTokenExpirationDate)
                .member(member)
                .refreshToken(refreshToken)
                .build();

        // TokenManager 저장
        tokenManagerRepository.save(tokenManager);

        return loginResponseDto;
    }

    @Transactional
    public void logOut(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = getJwtFromRequest(request, "X-Refresh-Token");

        if (refreshToken != null) {
            tokenManagerRepository.deleteByRefreshToken(refreshToken);

        }
    }

    @Transactional
    public void deleteUser(String email, HttpServletRequest request, HttpServletResponse response){
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("회원 정보가 없습니다."));

        String refreshToken = getJwtFromRequest(request, "X-Refresh-Token");
        if (refreshToken != null) {
            tokenManagerRepository.deleteByMemberId(member.getId());
        }
        memberRepository.delete(member);
    }




    // Request에서 JWT 토큰을 추출하는 메서드
    private String getJwtFromRequest(HttpServletRequest request, String tokenName) {
        return request.getHeader(tokenName);
    }
}
