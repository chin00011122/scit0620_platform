package com.scit0620.platform.attendance.domain;

import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
		name = "attendances",
		uniqueConstraints = @UniqueConstraint(name = "uk_attendance_event_member", columnNames = {
				"event_id", "member_id"
		})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private User member;

	@Column(nullable = false)
	private LocalDateTime checkedInAt;

	public Attendance(Event event, User member) {
		this.event = event;
		this.member = member;
		this.checkedInAt = LocalDateTime.now();
	}
}
