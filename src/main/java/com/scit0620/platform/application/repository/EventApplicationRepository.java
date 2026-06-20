package com.scit0620.platform.application.repository;

import com.scit0620.platform.application.domain.ApplicationStatus;
import com.scit0620.platform.application.domain.EventApplication;
import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventApplicationRepository extends JpaRepository<EventApplication, Long> {

	boolean existsByEventAndMember(Event event, User member);

	Optional<EventApplication> findByEventAndMember(Event event, User member);

	List<EventApplication> findByEvent(Event event);

	long countByStatus(ApplicationStatus status);
}
