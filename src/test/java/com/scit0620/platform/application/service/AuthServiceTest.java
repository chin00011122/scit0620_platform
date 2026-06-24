package com.scit0620.platform.application.service;


import com.scit0620.platform.auth.dto.AuthResponse;
import com.scit0620.platform.auth.dto.LoginRequest;
import com.scit0620.platform.auth.security.JwtTokenProvider;
import com.scit0620.platform.auth.service.AuthService;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.Role;
import com.scit0620.platform.user.domain.User;
import com.scit0620.platform.user.repository.UserRepository;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    //회원 레포지토리 관리
    @Mock
    private UserRepository userRepository;

    //비밀번호 검사
    @Mock
    private PasswordEncoder passwordEncoder;

    //토큰 발급
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    //로그인 담당
    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("비밀번호가 맞으면 로그인 성공 후 JWT토큰 발급")
    void loginSuccessTest() {
        //DB에 저장되어 있는 회원(암호화된 비밀번호)과 사용자가 입력한 로그인 폼을 만듦
        User member = new User("테스트", "test@test.com", "encoded1234", Role.MEMBER);
        LoginRequest request = new LoginRequest("test@test.com", "1234");

        //test@test.com을 찾으면 member를 반환
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(member));

        //방금 입력한 "1234"와 DB에 있던 "encoded1234"를 비교
        given(passwordEncoder.matches("1234", "encoded1234")).willReturn(true);

        //토큰 발급기에게 회원의 정보를 주면 "fake.jwt.token" 이라는 가짜 토큰을 줌
        given(jwtTokenProvider.createToken(member.getId(), member.getEmail(), member.getRole())).willReturn("fake.jwt.token");

        //로그인 요청
        AuthResponse response = authService.login(request);

        //결과로 받은 토큰이 우리가 조작한 가짜 토큰과 똑같은지 확인
        assertThat(response.token()).isEqualTo("fake.jwt.token");
    }

    @Test
    @DisplayName("비밀번호가 틀리면 예외가 발생해야 함")
    void loginWrongPasswordTest() {
        //DB에 저장되어 있는 회원(암호화된 비밀번호)과 사용자가 입력한 로그인 폼을 만듦
        User member = new User("테스트", "test@test.com", "encoded1234", Role.MEMBER);
        LoginRequest request = new LoginRequest("test@test.com", "9999");

        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(member));

        // 조작: "9999"와 "encoded1234"를 비교
        given(passwordEncoder.matches("9999", "encoded1234")).willReturn(false);

        //에러 발생
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(request);
        });

        // 에러 메시지가 정확한지 확인
        assertThat(exception.getMessage()).isEqualTo("이메일 또는 비밀번호가 올바르지 않습니다.");
    }
}
