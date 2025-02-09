package com.example.demo1.integration;

import com.example.demo1.dto.UserEditDto;
import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIT {

    @Value("${user.email}")
    private String TEST_EMAIL;
    @Value("${user.telegram}")
    private String TEST_TELEGRAM;

    private final MockMvc mockMvc;
    private final UserRepository userRepository;

    private UserEditDto dto;
    private User user;

    @BeforeEach
    void setUp() {
        if (userRepository.findByEmail(TEST_EMAIL).isEmpty()) {
            user = new User();
            user.setEmail(TEST_EMAIL);
            user.setTelegram(TEST_TELEGRAM);
            userRepository.save(user);
        }
        else {
            user = userRepository.findByEmail(TEST_EMAIL).get();
        }
        dto = new UserEditDto("newTelegram");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", TEST_EMAIL);
        OAuth2User oAuth2User = new DefaultOAuth2User(Collections.emptyList(), attributes, "email");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(oAuth2User, null, oAuth2User.getAuthorities()));
    }

    @Test
    void testEditUser() throws Exception {
        assertNotEquals(dto.telegram(), TEST_TELEGRAM);

        mockMvc.perform(put("/api/v1/profile/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                        .param("telegram", dto.telegram()))
                .andExpect(status().isOk());

        assertEquals(dto.telegram(), user.getTelegram());
    }
}
