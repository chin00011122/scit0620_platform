package com.scit0620.platform.event.controller;


import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.dto.EventResponse;
import com.scit0620.platform.event.service.EventService;
import com.scit0620.platform.user.domain.Role;
import com.scit0620.platform.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("행사 목록(GET /events)을 조회하면 200 OK와 JSON 데이터가 내려와야 함")
    void getEventsTest() throws Exception {
        User admin = new User("관리자", "admin", "0000", Role.ADMIN);
        Event event = new Event("MockMvc 축제", "설명", "장소", LocalDateTime.now(), 50, admin);

        EventResponse fakeResponse = EventResponse.from(event);
        //누가 getEvents()를 요구하면, 행사 리스트 반환
        given(eventService.getEvents()).willReturn(List.of(fakeResponse));

        mockMvc.perform(get("/events")) //events 를 요청
                .andExpect(status().isOk()) //결과는 무조건 200 OK(정상)
                .andExpect(jsonPath("$[0].title").value("MockMvc 축제")); //응답받은 JSON의 첫 번째([0]) 제목(title)이 "MockMvc 축제" 여야함.

    }
}
