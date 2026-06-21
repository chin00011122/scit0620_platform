package com.scit0620.platform.feedback.domain;

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
// 행사 피드백 테이블이다. MVP에서는 회원당 행사별 피드백을 한 번만 허용한다.
@Table(
		name = "feedbacks",
		uniqueConstraints = @UniqueConstraint(name = "uk_feedback_event_member", columnNames = {
				"event_id", "member_id"
		})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 피드백은 특정 행사에 대한 평가다.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	// 피드백을 작성한 회원이다.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private User member;

	@Column(nullable = false)
	private int rating;

	@Column(nullable = false, columnDefinition = "text")
	private String content;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	public Feedback(Event event, User member, int rating, String content) {
		this.event = event;
		this.member = member;
		this.rating = rating;
		this.content = content;
		this.createdAt = LocalDateTime.now();
	}
}
