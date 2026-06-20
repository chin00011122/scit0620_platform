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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EventStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_id", nullable = false)
	private User createdBy;

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
}
