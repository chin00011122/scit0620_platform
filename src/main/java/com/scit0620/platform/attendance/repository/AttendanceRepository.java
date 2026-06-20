package com.scit0620.platform.attendance.repository;

import com.scit0620.platform.attendance.domain.Attendance;
import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	boolean existsByEventAndMember(Event event, User member);
}
