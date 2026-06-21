package com.scit0620.platform.global.config;

import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.repository.EventRepository;
import com.scit0620.platform.user.domain.Role;
import com.scit0620.platform.user.domain.User;
import com.scit0620.platform.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DemoDataLoader implements CommandLineRunner {

	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final PasswordEncoder passwordEncoder;

	public DemoDataLoader(UserRepository userRepository, EventRepository eventRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (userRepository.existsByEmail("admin@setuyeon.kr")) {
			return;
		}

		User admin = userRepository.save(new User(
				"세투연 회장",
				"admin@setuyeon.kr",
				passwordEncoder.encode("password123!"),
				Role.ADMIN
		));
		userRepository.save(new User("운영진 김세미", "staff@setuyeon.kr", passwordEncoder.encode("password123!"), Role.STAFF));
		userRepository.save(new User("부원 이투자", "member@setuyeon.kr", passwordEncoder.encode("password123!"), Role.MEMBER));

		Event ot = new Event(
				"세투연 신입 부원 OT",
				"세종대학교 투자동아리 연합 신입 부원을 위한 활동 소개와 운영 안내 행사입니다.",
				"세종대학교 광개토관 101호",
				LocalDateTime.now().plusDays(3),
				80,
				admin
		);
		ot.open();
		eventRepository.save(ot);

		Event seminar = new Event(
				"기업분석 세미나 - 반도체 산업",
				"반도체 업종의 실적 지표와 밸류에이션을 함께 분석하는 세미나입니다.",
				"세종대학교 대양AI센터 B205",
				LocalDateTime.now().plusDays(7),
				40,
				admin
		);
		seminar.open();
		eventRepository.save(seminar);
	}
}
