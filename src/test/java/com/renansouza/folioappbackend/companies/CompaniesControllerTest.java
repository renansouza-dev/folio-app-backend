package com.renansouza.folioappbackend.companies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renansouza.folioappbackend.companies.exceptions.CompaniesAlreadyExistsException;
import com.renansouza.folioappbackend.companies.exceptions.CompaniesNotFoundException;
import com.renansouza.folioappbackend.companies.models.CompaniesRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.renansouza.folioappbackend.companies.Companies.getCompaniesRequest;
import static com.renansouza.folioappbackend.companies.Companies.getCompaniesResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompaniesController.class)
class CompaniesControllerTest {

    private static final String PATH = "/v1/companies";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CompaniesService service;

    @Test
    public void getAllCompanies() throws Exception {
        // given - precondition or setup
        var companiesResponse = getCompaniesResponse();
        when(service.getCompanies(any(), any(), any())).thenReturn(new PageImpl<>(List.of(companiesResponse)));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(getCompaniesRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.last", is(true)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.content.[0].id", is(companiesResponse.id().toString())))
                .andExpect(jsonPath("$.content.[0].name", is(companiesResponse.name())))
                .andExpect(jsonPath("$.content.[0].cnpj", is(companiesResponse.cnpj())))
                .andExpect(jsonPath("$.content.[0].broker", is(companiesResponse.broker())))
                .andExpect(jsonPath("$.content.[0].listed", is(companiesResponse.listed())));
    }

    @Test
    public void getNoCompanies() throws Exception {
        // given - precondition or setup
        when(service.getCompanies(any(), any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(getCompaniesRequest())))
                .andExpect(status().isNoContent());
    }

    @Test
    public void createCompany() throws Exception {
        // given - precondition or setup
        doNothing().when(service).createCompanies(any(CompaniesRequest.class));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(getCompaniesRequest())))
                .andExpect(status().isCreated());
    }

    @Test
    public void failToCreateCompany() throws Exception {
        // given - precondition or setup
        doThrow(new CompaniesAlreadyExistsException()).when(service).createCompanies(any(CompaniesRequest.class));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(getCompaniesRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.code", is("CONFLICT")))
                .andExpect(jsonPath("$.message", is("A company with the provided data already exists.")));
    }

    @Test
    public void updateCompany() throws Exception {
        // given - precondition or setup
        doNothing().when(service).updateCompanies(any(UUID.class), any(CompaniesRequest.class));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(patch(PATH + "/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(getCompaniesRequest())))
                .andExpect(status().isNoContent());
    }

    @Test
    public void failToUpdateCompany() throws Exception {
        // given - precondition or setup
        doThrow(new CompaniesNotFoundException())
                .when(service).updateCompanies(any(UUID.class), any(CompaniesRequest.class));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(patch(PATH + "/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(getCompaniesRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.code", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("No company found with provided id.")));
    }

    @Test
    public void deleteCompany() throws Exception {
        // given - precondition or setup
        willDoNothing().given(service).deleteCompanies(any(UUID.class));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(delete(PATH + "/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void failToDeleteCompany() throws Exception {
        // given - precondition or setup
        doThrow(new CompaniesNotFoundException()).when(service).deleteCompanies(any(UUID.class));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(delete(PATH + "/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.code", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("No company found with provided id.")));
    }

}