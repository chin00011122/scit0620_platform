package com.scit0620.platform.event.domain;

import com.scit0620.platform.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
// 행사 운영 플랫폼의 중심 테이블이다. 신청, 출석, 피드백은 모두 특정 행사에 연결된다.
@Table(name = "events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String title;

	@Column(nullable = false, columnDefinition = "text")
	private String description;

	@Column(nullable = false, length = 120)
	private String location;

	@Column(nullable = false)
	private LocalDateTime eventDateTime;

	@Column(nullable = false)
	private int capacity;

	// DRAFT -> OPEN -> CLOSED -> COMPLETED 흐름을 문자열 enum으로 DB에 저장한다.
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EventStatus status;

	// 행사를 만든 관리자 또는 운영진이다. LAZY는 실제로 필요할 때만 User를 조회하게 한다.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_id", nullable = false)
	private User createdBy;

	// 운영진이 발급하고 회원이 입력하는 출석 코드다. MVP에서는 별도 테이블 대신 Event에 둔다.
	@Column(length = 20)
	private String attendanceCode;

	private LocalDateTime attendanceCodeExpiresAt;

	public Event(String title, String description, String location, LocalDateTime eventDateTime, int capacity,
			User createdBy) {
		this.title = title;
		this.description = description;
		this.location = location;
		this.eventDateTime = eventDateTime;
		this.capacity = capacity;
		this.createdBy = createdBy;
		this.status = EventStatus.DRAFT;
	}

	// setter 대신 업무 의미가 드러나는 메서드로 행사 상태를 변경한다.
	public void open() {
		this.status = EventStatus.OPEN;
	}

	public void close() {
		this.status = EventStatus.CLOSED;
	}

	public void complete() {
		this.status = EventStatus.COMPLETED;
	}

	public void issueAttendanceCode(String attendanceCode, LocalDateTime expiresAt) {
		this.attendanceCode = attendanceCode;
		this.attendanceCodeExpiresAt = expiresAt;
	}

	public boolean isOpen() {
		return this.status == EventStatus.OPEN;
	}

	public boolean isAttendanceCodeValid(String code, LocalDateTime now) {
		return this.attendanceCode != null
				&& this.attendanceCode.equals(code)
				&& this.attendanceCodeExpiresAt != null
				&& this.attendanceCodeExpiresAt.isAfter(now);
	}
}
