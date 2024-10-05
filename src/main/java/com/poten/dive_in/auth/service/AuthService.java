package com.poten.dive_in.auth.service;

import com.poten.dive_in.auth.dto.KakaoAccountDto;
import com.poten.dive_in.auth.dto.LoginResponseDto;
import com.poten.dive_in.auth.dto.MemberInfoDto;
import com.poten.dive_in.auth.enums.SocialType;
import com.poten.dive_in.auth.jwt.JwtTokenProvider;
import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.entity.TokenManager;
import com.poten.dive_in.auth.enums.Role;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.auth.repository.TokenManagerRepository;
import com.poten.dive_in.common.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.metal.MetalMenuBarUI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.poten.dive_in.common.service.S3Service.extractFileName;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenManagerRepository tokenManagerRepository;
    private final HttpServletResponse httpServletResponse;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public MemberInfoDto getCurrentMemberInfo(String email){

        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("사용자가 존재하지 않습니다."));
        return MemberInfoDto.ofEntity(member);
    }

    @Transactional
    public MemberInfoDto updateCurrentMemberInfo(String email, MemberInfoDto memberInfoDto, MultipartFile file) {

        // 해당 email의 member가 존재하는지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));

        // email의 member가 DTO의 email과 같은지 확인
        if (!member.getEmail().equals(memberInfoDto.getEmail())) {
            throw new EntityNotFoundException("수정 권한이 없습니다.");
        }

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

        member.updateMember(memberInfoDto, profileImageUrl);
        return MemberInfoDto.ofEntity(member);
    }


    @Transactional
    public LoginResponseDto login(KakaoAccountDto kakaoAccountDto){

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
        // 쿠키에서 refresh_token 추출
        String refreshToken = getJwtFromRequest(request, "refresh_token");

        if (refreshToken != null) {
            tokenManagerRepository.deleteByRefreshToken(refreshToken);

            // 클라이언트의 쿠키 삭제
            deleteCookies(response);
        }
    }


    public void deleteCurrentMember(String email, HttpServletRequest request, HttpServletResponse response){
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("회원 정보가 없습니다."));

        String refreshToken = getJwtFromRequest(request, "refresh_token");
        if (refreshToken != null) {
            tokenManagerRepository.deleteByRefreshToken(refreshToken);

            // 클라이언트의 쿠키 삭제
            deleteCookies(response);
        }
        memberRepository.delete(member);
    }


    public void deleteCookies(HttpServletResponse response) {
        // Access Token 쿠키 삭제
        response.addHeader("Set-Cookie", "access_token=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0;");

        // Refresh Token 쿠키 삭제
        response.addHeader("Set-Cookie", "refresh_token=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0;");

    }


    private String getJwtFromRequest(HttpServletRequest request, String token_name) {
        // 쿠키에서 JWT 토큰을 찾기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(token_name)) { // Access Token 쿠키에서 가져오기
                    return cookie.getValue(); // 쿠키 값 반환
                }
            }
        }
        return null;
    }
}
