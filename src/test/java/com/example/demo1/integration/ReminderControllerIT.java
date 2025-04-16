package com.example.demo1.integration;

import com.example.demo1.dto.RequestReminderDto;
import com.example.demo1.entity.Reminder;
import com.example.demo1.entity.User;
import com.example.demo1.repository.ReminderRepository;
import com.example.demo1.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReminderControllerIT {

    @Value("${user.email}")
    private String TEST_EMAIL;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final Scheduler scheduler;
    private final UserRepository userRepository;
    private final ReminderRepository reminderRepository;

    private RequestReminderDto requestDto;
    private Long reminderId;
    private User user;

    @BeforeEach
    void setUp() {
        if (userRepository.findByEmail(TEST_EMAIL).isEmpty()) {
            user = new User();
            user.setEmail(TEST_EMAIL);
            userRepository.save(user);
        }
        else {
            user = userRepository.findByEmail(TEST_EMAIL).get();
        }
        requestDto = new RequestReminderDto("test", null, null);


        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", TEST_EMAIL);
        OAuth2User oAuth2User = new DefaultOAuth2User(Collections.emptyList(), attributes, "email");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(oAuth2User, null, oAuth2User.getAuthorities()));
    }

    @Test
    @Order(1)
    void testSaveReminder() throws Exception {
        Reminder reminder = new Reminder();
        reminder.setTitle("Test Reminder");
        reminder.setDescription("Test Reminder");
        reminder.setRemind(LocalDateTime.now().plusMonths(1));
        reminder.setUser(user);

        String reminderJson = objectMapper.writeValueAsString(reminder);

        MvcResult result = mockMvc.perform(post("/api/v1/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reminderJson))
                .andExpect(status().isCreated())
                .andReturn();

        Reminder createdReminder = objectMapper.readValue(result.getResponse().getContentAsString(), Reminder.class);
        reminderId = createdReminder.getId();

        Set<JobKey> jobKeyList = scheduler.getJobKeys(GroupMatcher.anyGroup());
        Set<String> jobKeyNames = new HashSet<>();
        for (JobKey jobKey : jobKeyList) {
            jobKeyNames.add(jobKey.getName());
        }

        assertTrue(jobKeyNames.contains("emailJob_" + reminderId));
        assertTrue(jobKeyNames.contains("telegramJob_" + reminderId));
    }

    @Test
    @Order(2)
    void testGetRemindersByFilter() throws Exception {
        mockMvc.perform(get("/api/v1/reminders")
                        .param("query", requestDto.query())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Reminder"))
                .andExpect(jsonPath("$.content[0].description").value("Test Reminder"));
    }

    @Test
    @Order(3)
    void testEditReminder() throws Exception {
        Reminder reminder = reminderRepository.findById(reminderId).get();

        assertNotEquals("Old test reminder", reminder.getTitle());
        assertNotEquals("Old test reminder", reminder.getDescription());

        mockMvc.perform(put("/api/v1/reminders/" + reminderId)
                .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "Old test reminder")
                        .param("description", "Old test reminder"))
                .andExpect(status().isOk());

        reminder = reminderRepository.findById(reminderId).get();

        assertEquals("Old test reminder", reminder.getTitle());
        assertEquals("Old test reminder", reminder.getDescription());
    }

    @Test
    @Order(4)
    void testDeleteReminder() throws Exception {
        assertTrue(reminderRepository.existsById(reminderId));

        mockMvc.perform(delete("/api/v1/reminders/" + reminderId))
                .andExpect(status().isOk());

        assertFalse(reminderRepository.existsById(reminderId));

        Set<JobKey> jobKeyList = scheduler.getJobKeys(GroupMatcher.anyGroup());
        Set<String> jobKeyNames = new HashSet<>();
        for (JobKey jobKey : jobKeyList) {
            jobKeyNames.add(jobKey.getName());
        }

        assertFalse(jobKeyNames.contains("emailJob_" + reminderId));
        assertFalse(jobKeyNames.contains("telegramJob_" + reminderId));
    }
}
