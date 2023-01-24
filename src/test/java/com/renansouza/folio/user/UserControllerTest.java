package com.renansouza.folio.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private static String asJsonString(final Object obj) {
        try {
            var objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should not find an user")
    void notFindById() throws Exception {
        when(service.findById(anyLong())).thenThrow(new UserNotFoundException(1L));

        var id = 1L;
        var message = "Could not found an user with id " + id;

        mockMvc.perform(get("/user/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals(message, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("should find an user")
    void findById() throws Exception {
        var user = new User("User", "avatar");

        when(service.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/user/" + user.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    @DisplayName("should add a new user")
    void add() throws Exception {
        when(service.add(any(User.class))).thenReturn(null);
        var user = new User("Username", null);

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("should not add a new user")
    void failToAdd() throws Exception {
        var user = new User("Username", "avatar");
        var message = "An user with the name " + user.getName() + " already exists";

        when(service.add(any(User.class))).thenThrow(new UserAlreadyExistsException(user.getName()));

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAlreadyExistsException))
                .andExpect(result -> assertEquals(message, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("should update a new user")
    void update() throws Exception {
        var user = new User("Username", "https://www.example.com");
        Mockito.doNothing().when(service).update(any(User.class));

        mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should not update a new user")
    void failToUpdate() throws Exception {
        var user = new User("Username", "avatar");
        user.setId(1);

        var message = "Could not found an user with id " + user.getId();
        Mockito.doThrow(new UserNotFoundException(user.getId())).when(service).update(any(User.class));

        mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals(message, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}