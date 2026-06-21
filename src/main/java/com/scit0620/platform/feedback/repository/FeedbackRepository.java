package com.scit0620.platform.feedback.repository;

import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.feedback.domain.Feedback;
import com.scit0620.platform.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

	boolean existsByEventAndMember(Event event, User member);

	long countByEvent(Event event);
}
