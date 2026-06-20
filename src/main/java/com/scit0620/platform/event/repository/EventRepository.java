package com.scit0620.platform.event.repository;

import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.domain.EventStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByStatus(EventStatus status);
}
