package com.scit0620.platform.application.service;


import com.scit0620.platform.application.domain.EventApplication;
import com.scit0620.platform.application.repository.EventApplicationRepository;
import com.scit0620.platform.auth.security.UserPrincipal;
import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.dto.EventResponse;
import com.scit0620.platform.event.repository.EventRepository;
import com.scit0620.platform.event.service.EventService;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.Role;
import com.scit0620.platform.user.domain.User;
import com.scit0620.platform.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class EventApplicationServiceTest {
    //@Mock 가짜 DB(레포지토리)를 만듦. 진짜 DB를 쓰면 너무 느리고 복잡해짐.
    @Mock //회원 관리
    private UserRepository userRepository;

    @Mock //행사 서비스 관리
    private EventService eventService;

    @Mock //행사 신청 관리
    private EventApplicationRepository applicationRepository;
    @InjectMocks
    private EventApplicationService applicationService;

    @Test
    @DisplayName("일반 회원이 정상적으로 행사 참가 신청 할 수 있음")
    void applyForEventTest() {
        User member = new User("일반유저", "member", "0000", Role.MEMBER);
        Event event = new Event("테스트 행사", "설명", "장소", LocalDateTime.now().plusDays(1), 50, member);
        event.open(); // 모집 상태로 변경


        // Spring Security에서 사용하는 가짜 인증서(UserPrincipal) 생성
        UserPrincipal principal = new UserPrincipal(1L, "member@test.com", Role.MEMBER);

        //누가 1번 회원을 찾을 경우 membe반환
        given(userRepository.findById(1L)).willReturn(Optional.of(member));
        //누가 1번 행사 찾으면 event반환
        given(eventService.getEventEntity(1L)).willReturn(event);

        //신청 내역 저장save 시뮬레이션
        EventApplication savedApplication = new EventApplication(event, member);

        given(applicationRepository.save(any(EventApplication.class))).willReturn(savedApplication);
        //1번 행사에 member의 인증서(principal)을 들고 신청
        applicationService.apply(1L, principal);
    }

    @Test
    @DisplayName("모집이 끝난 행사에 신청하면 에러가 발생해야 함")
    void applyForClosedEventTest() {
        //when 행사를 만들고 CLOSED 상태로 둠
        User member = new User("일반유저", "member", "0000", Role.MEMBER);
        Event closedEvent = new Event("마감된 행사", "설명", "장소", LocalDateTime.now().plusDays(1), 50, member);
        closedEvent.close(); //행사를 강제로 마감시킴

        UserPrincipal principal = new UserPrincipal(1L, "member@test.com", Role.MEMBER);

        //given 1번 회원을 찾으면 member를 주고, 1번 행사를 찾으면 closedEvent를 줌
        given(userRepository.findById(1L)).willReturn(Optional.of(member));
        given(eventService.getEventEntity(1L)).willReturn(closedEvent);

        //assertThrows안에 있는 코드 applicationService.apply(1L, principal);를 싱행하면
        // 예외를 던지는 확인하고 그 예외 타입이 BusinessException인지 확인
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            applicationService.apply(1L, principal);
        });
        //에러 메시지 확인
        assertThat(exception.getMessage()).isEqualTo("모집중인 행사만 신청할 수 있습니다.");
    }
}

