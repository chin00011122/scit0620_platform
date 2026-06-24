package com.scit0620.platform.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
// DB의 사용자 테이블이다. PostgreSQL에서 user는 예약어로 충돌할 수 있어 users라는 이름을 쓴다.
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, unique = true, length = 120)
	private String email;

	@Column(nullable = false)
	private String password;

	// MEMBER, STAFF, ADMIN 중 하나만 저장되도록 enum으로 제한
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Role role;

	public User(String name, String email, String password, Role role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public boolean hasRole(Role role) {
		return this.role == role;
	}
}
