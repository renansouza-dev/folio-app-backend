package com.renansouza.folio.fixedincome;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.renansouza.folio.fixedincome.entities.FixedIncome;
import com.renansouza.folio.fixedincome.exception.FixedIncomeNotFoundException;
import org.instancio.Instancio;
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

import java.util.Collections;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FixedIncomeControllerTest {

    public static final String FIXED_INCOME_PATH = "/fixed_income";
    @Mock
    FixedIncomeService service;

    @Mock
    FixedIncomeAdvice advice;

    @InjectMocks
    FixedIncomeController controller;

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
    @DisplayName("should not find any fixed incomes")
    void all() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(FIXED_INCOME_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should not find an fixed income")
    void notFindById() throws Exception {
        when(service.findById(anyLong())).thenThrow(new FixedIncomeNotFoundException(1L));

        var id = 1L;
        var message = "Could not found an fixed income with id " + id;

        mockMvc.perform(get(FIXED_INCOME_PATH + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FixedIncomeNotFoundException))
                .andExpect(result -> assertEquals(message, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("should find an fixed income")
    void findById() throws Exception {
        var fixedIncome = Instancio.of(FixedIncome.class).create();

        when(service.findById(anyLong())).thenReturn(fixedIncome);

        mockMvc.perform(get(FIXED_INCOME_PATH + "/" + fixedIncome.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brokerage", is(fixedIncome.getBrokerage())));
    }

    @Test
    @DisplayName("should add a new fixed income")
    void add() throws Exception {
        when(service.add(any(FixedIncome.class))).thenReturn(null);
        var fixedIncome = Instancio.of(FixedIncome.class).create();

        mockMvc.perform(post(FIXED_INCOME_PATH).contentType(MediaType.APPLICATION_JSON).content(asJsonString(fixedIncome)))
                .andExpect(status().isCreated());
    }

}