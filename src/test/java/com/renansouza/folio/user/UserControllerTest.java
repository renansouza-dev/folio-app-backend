package com.renansouza.folio.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService service;

    @Mock
    UserAdvice advice;

    @InjectMocks
    UserController controller;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(advice)
                .build();
    }

    @Test
    @DisplayName("should not find any users")
    void all() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should not find an user")
    void notFindById() throws Exception {
        when(service.findById(anyLong())).thenThrow(new UserNotFoundException(1L));

        var id = 1L;
        var message = "Could not found an user with id " + id;

        mockMvc.perform(get("/user/" + id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("should find an user")
    void findById() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        var user = new User(1, "User", null, now, now);

        when(service.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/user/" + user.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    @DisplayName("should add a new user")
    void add() throws Exception {
        when(service.add(any(UserForm.class))).thenReturn(null);
        var form = new UserForm("User", null);

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(form)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should not add a new user")
    void failToAdd() throws Exception {
        var form = new UserForm("User", null);
        var message = "An user with the name " + form.getName() + " already exists";

        when(service.add(any(UserForm.class))).thenThrow(new UserAlreadyExistsException(form.getName()));

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(form)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAlreadyExistsException))
                .andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}