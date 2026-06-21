package com.scit0620.platform.auth.service;

import com.scit0620.platform.auth.dto.AuthResponse;
import com.scit0620.platform.auth.dto.LoginRequest;
import com.scit0620.platform.auth.dto.SignupRequest;
import com.scit0620.platform.auth.security.JwtTokenProvider;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.Role;
import com.scit0620.platform.user.domain.User;
import com.scit0620.platform.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Transactional
	public AuthResponse signup(SignupRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new BusinessException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");
		}

		Role role = request.role() == null ? Role.MEMBER : request.role();
		User user = new User(
				request.name(),
				request.email(),
				passwordEncoder.encode(request.password()),
				role
		);
		User savedUser = userRepository.save(user);
		return createResponse(savedUser);
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BusinessException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다.");
		}

		return createResponse(user);
	}

	private AuthResponse createResponse(User user) {
		String token = jwtTokenProvider.createToken(user.getId(), user.getEmail(), user.getRole());
		return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
	}
}
