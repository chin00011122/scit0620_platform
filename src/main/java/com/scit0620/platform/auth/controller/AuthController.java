package com.scit0620.platform.auth.controller;

import com.scit0620.platform.auth.dto.AuthResponse;
import com.scit0620.platform.auth.dto.LoginRequest;
import com.scit0620.platform.auth.dto.SignupRequest;
import com.scit0620.platform.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController  // JSON 데이터를 주고받는 웹 API용 컨트롤러
@RequestMapping("/auth")  // 이 컨트롤러의 모든 API 주소는 /auth로 시작하도록 설정
public class AuthController {

	private final AuthService authService;  //실제 회원가입/로그인 비즈니스 로직을 처리할 서비스

	// 생성자를 통해 의존성 주입(DI)으로 서비스 객체를 주입받아 연결함
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/signup") // POST 방식으로 /auth/signup 주소에 요청이 들어오면 실행됨
	@ResponseStatus(HttpStatus.CREATED) //회원가입에 성공하면 HTTP 응답 코드로 201(Created) 반환
	public AuthResponse signup(@RequestBody SignupRequest request) {
		return authService.signup(request);
	}
	// @RequestBody: 클라이언트가 보낸 JSON 형식의 회원정보를 SignupRequest DTO 객체로 변환하여 수령

	@PostMapping("/login")
	public AuthResponse login(@RequestBody LoginRequest request) {
		return authService.login(request);
	}
	// 서비스의 로그인 비즈니스 로직을 실행하고 결과를 반환함 (성공 시 기본값인 200 OK가 나감)
}
