package com.personal.laneheroes.controllers;

import com.personal.laneheroes.services.*;
import com.personal.laneheroes.utilities.JwtUtil;
import com.personal.laneheroes.utilities.ResponseMessages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({AdminControllerTest.ServiceMockConfig.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @TestConfiguration
    static class ServiceMockConfig {
        @Bean
        public AdminService adminService() {
            return mock(AdminService.class);
        }

        @Bean
        public JwtUtil jwtUtil() {
            return mock(JwtUtil.class);
        }

        @Bean
        public UserDetailsService userDetailsService(){
            return mock(UserDetailsService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    void uploadAllData_1() throws Exception {
        // Prepare mock files
        MockMultipartFile company = new MockMultipartFile("companyFile", "company.xlsx", "application/vnd.ms-excel", "fake-data".getBytes());
        MockMultipartFile callsign = new MockMultipartFile("callsignFile", "callsign.xlsx", "application/vnd.ms-excel", "fake-data".getBytes());
        MockMultipartFile platform = new MockMultipartFile("platformFile", "platform.xlsx", "application/vnd.ms-excel", "fake-data".getBytes());
        MockMultipartFile game = new MockMultipartFile("gameFile", "game.xlsx", "application/vnd.ms-excel", "fake-data".getBytes());
        MockMultipartFile hero = new MockMultipartFile("heroFile", "hero.xlsx", "application/vnd.ms-excel", "fake-data".getBytes());
        MockMultipartFile skill = new MockMultipartFile("skillFile", "skill.xlsx", "application/vnd.ms-excel", "fake-data".getBytes());

        // Mock service call
        when(adminService.uploadAllData(
                any(String.class),
                any(String.class),
                any(String.class),
                any(String.class),
                any(String.class),
                any(String.class))
        ).thenReturn("All uploaded!");

        when(jwtUtil.extractUsername(any())).thenReturn("admin");
        when(jwtUtil.isTokenValid(any())).thenReturn(true);

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(User.builder()
                .username("admin")
                .password("password") // not validated during JWT check
                .roles("ADMIN")
                .build());

        // Perform the request
        mockMvc.perform(multipart("/laneHeroes/api/admin/batch-upload-all")
                        .file(company)
                        .file(callsign)
                        .file(platform)
                        .file(game)
                        .file(hero)
                        .file(skill)
                        .header("Authorization", "Bearer faketkne")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseMessages.SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value("Upload Complete"))
                .andExpect(jsonPath("$.data").value("All uploaded!"));
    }

    @Test
    void uploadAllData_2() throws Exception {
        MockMultipartFile dummy = new MockMultipartFile("companyFile", "company.xlsx", "application/vnd.ms-excel", "bad".getBytes());

        // Send all 5 files
        when(adminService.uploadAllData(any(), any(), any(), any(), any(),any()))
                .thenThrow(new RuntimeException("Boom"));

        when(jwtUtil.extractUsername(any())).thenReturn("admin");
        when(jwtUtil.isTokenValid(any())).thenReturn(true);

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(User.builder()
                .username("admin")
                .password("password") // not validated during JWT check
                .roles("ADMIN")
                .build());

        mockMvc.perform(multipart("/laneHeroes/api/admin/batch-upload-all")
                        .file("companyFile", dummy.getBytes())
                        .file("callsignFile", dummy.getBytes())
                        .file("platformFile", dummy.getBytes())
                        .file("gameFile", dummy.getBytes())
                        .file("heroFile", dummy.getBytes())
                        .file("skillFile", dummy.getBytes())
                        .header("Authorization", "Bearer faketkne")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(ResponseMessages.FAIL_STATUS))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Upload failed")));
    }
}
