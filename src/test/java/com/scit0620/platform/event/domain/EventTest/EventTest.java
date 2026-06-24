package com.scit0620.platform.event.domain.EventTest;

import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.domain.EventStatus;
import com.scit0620.platform.user.domain.Role;
import com.scit0620.platform.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {
    @Test
    @DisplayName("행사를 open()하면 상태가 OPEN으로 변경되어야 한다")
    void openEventTest(){
        //given (준비 단계: 가짜 유저와 가짜 행사를 만든다)
        User admin  = new User("김관리", "admin", "0000", Role.ADMIN);

        Event event= new Event(
                "테스트 행사",
                "테스트 설명",
                "장소",
                LocalDateTime.now().plusDays(1),
                50,
                admin
        );
        //when실행: 상태를 모집중OPEN으로 바꾸는 실제 메소드를 호출함
        event.open();

        //then검증: 정말로 상태가 OPEN모집중으로 바뀌었는지 확인한다.
        assertThat(event.getStatus()).isEqualTo(EventStatus.OPEN);

    }
}
