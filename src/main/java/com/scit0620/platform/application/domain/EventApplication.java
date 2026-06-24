package com.scit0620.platform.application.domain;

import com.scit0620.platform.event.domain.Event;
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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
// 회원의 행사 신청 테이블. 한 회원은 같은 행사에 한 번만 신청 가능
@Table(
		name = "event_applications",
		uniqueConstraints = @UniqueConstraint(name = "uk_event_application_event_member", columnNames = {
				"event_id", "member_id"
		})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 여러 신청이 하나의 행사에 속하므로 ManyToOne 관계.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	// 여러 신청이 하나의 회원에 속하므로 ManyToOne 관계.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private User member;

	// 신청은 처음에 PENDING으로 생성되고 운영진이 APPROVED 또는 REJECTED로 변경.
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ApplicationStatus status;

	@Column(nullable = false)
	private LocalDateTime appliedAt;

	private LocalDateTime decidedAt;

	public EventApplication(Event event, User member) {
		this.event = event;
		this.member = member;
		this.status = ApplicationStatus.PENDING;
		this.appliedAt = LocalDateTime.now();
	}

	// 승인 처리 시 상태와 결정 시각을 함께 기록.
	public void approve() {
		this.status = ApplicationStatus.APPROVED;
		this.decidedAt = LocalDateTime.now();
	}

	// 반려 처리 시 상태와 결정 시각을 함께 기록한다.
	public void reject() {
		this.status = ApplicationStatus.REJECTED;
		this.decidedAt = LocalDateTime.now();
	}

	public boolean isApproved() {
		return this.status == ApplicationStatus.APPROVED;
	}
}
