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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private User member;

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

	public void approve() {
		this.status = ApplicationStatus.APPROVED;
		this.decidedAt = LocalDateTime.now();
	}

	public void reject() {
		this.status = ApplicationStatus.REJECTED;
		this.decidedAt = LocalDateTime.now();
	}
}
