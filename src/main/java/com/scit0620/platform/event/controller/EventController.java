package com.scit0620.platform.event.controller;

import com.scit0620.platform.auth.security.CurrentUser;
import com.scit0620.platform.event.dto.AttendanceCodeResponse;
import com.scit0620.platform.event.dto.EventCreateRequest;
import com.scit0620.platform.event.dto.EventResponse;
import com.scit0620.platform.event.dto.EventStatusUpdateRequest;
import com.scit0620.platform.event.service.EventService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//행사 관련 요청 처리 컨트롤러
@RestController // JSON 데이터를 주고받는 API 컨트롤러(웨이터) 선언
@RequestMapping("/events")
public class EventController {

	private final EventService eventService; // 행사 관련 비즈니스 로직을 처리하는 서비스

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}
	// 생성자를 통해 의존성 주입(DI)으로 서비스를 연결

	@PostMapping // POST 방식으로 /events 주소에 요청이 오면 실행. 새 행사 생성
	@ResponseStatus(HttpStatus.CREATED) //행사 성공 시 HTTP 상태 코드 201 Created를 반환
	public EventResponse createEvent(@RequestBody EventCreateRequest request) {
		// @RequestBody: 클라이언트가 보낸 JSON 데이터를 EventCreateRequest 객체로 변환하여 수령
		return eventService.createEvent(request, CurrentUser.get());
		// CurrentUser.get(): 현재 로그인한 유저 정보를 가져와서 서비스에 함께 넘겨줌 (행사 등록자 설정을 위함)
	}

	@GetMapping// GET 방식으로 /events 주소에 요청이 오면 실행 (행사 목록 전체 조회)
	public List<EventResponse> getEvents() {
		return eventService.getEvents();
	}

	@GetMapping("/{eventId}")// GET 방식으로 /events/{행사ID} 주소에 요청이 오면 실행 (행사 상세 단건 조회)
	public EventResponse getEvent(@PathVariable Long eventId) {
		return eventService.getEvent(eventId);
	}
	// @PathVariable: 주소창의 {eventId} 자리에 들어오는 숫자를 자바의 eventId 변수에 바인딩함

	@PatchMapping("/{eventId}/status") // PATCH 방식으로 /events/{행사ID}/status 주소에 요청이 오면 실행 (행사 상태 수정)
	public EventResponse updateStatus(@PathVariable Long eventId, @RequestBody EventStatusUpdateRequest request) {
		// 경로의 eventId와 바디(Body)의 수정 내용(request)을 조합해 서비스에 전달
		return eventService.updateStatus(eventId, request, CurrentUser.get());
	}

	@PostMapping("/{eventId}/attendance-code") // POST 방식으로 /events/{행사ID}/attendance-code 주소에 요청이 오면 실행 (출석 코드 발급)
	public AttendanceCodeResponse issueAttendanceCode(@PathVariable Long eventId) {
		return eventService.issueAttendanceCode(eventId, CurrentUser.get());
	}
}
