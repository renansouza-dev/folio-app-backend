package com.renansouza.folioappbackend.companies;

import com.renansouza.folioappbackend.companies.exceptions.CompaniesAlreadyExistsException;
import com.renansouza.folioappbackend.companies.exceptions.CompaniesNotFoundException;
import com.renansouza.folioappbackend.companies.models.CompaniesEntity;
import com.renansouza.folioappbackend.companies.models.CompaniesResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.renansouza.folioappbackend.companies.Companies.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompaniesServiceTest {

    @Mock
    CompaniesRepository repository;

    @InjectMocks
    CompaniesService service;

    @Test
    public void getAllCompany() {
        // given - precondition or setup
        when(repository.findAllCompanies(any(Pageable.class))).thenReturn(Page.empty());

        // when - action or the behaviour that we are going test
        Page<CompaniesResponse> companies = service.getCompanies(null, null, Pageable.unpaged());

        // then
        verify(repository, times(1)).findAllCompanies(any(Pageable.class));
        assertThat(companies, notNullValue());
        assertThat(companies.getTotalPages(), is(1));
        assertThat(companies.getTotalElements(), is(0L));
        assertThat(companies.getContent().size(), is(0));
        assertThat(companies.getNumberOfElements(), is(0));
    }

    @Test
    public void getByCompanyId() {
        // given - precondition or setup
        var companiesResponse = getCompaniesResponse();
        var page = new PageImpl<>(List.of(companiesResponse));
        when(repository.findAllByIdOrCnpj(any(UUID.class), any(), any(Pageable.class))).thenReturn(page);

        // when - action or the behaviour that we are going test
        var companies = service.getCompanies(companiesResponse.id(), null, Pageable.unpaged());

        // then
        verify(repository, times(1)).findAllByIdOrCnpj(any(UUID.class), any(), any(Pageable.class));
        assertThat(companies, notNullValue());
        assertThat(companies.getTotalPages(), is(1));
        assertThat(companies.getTotalElements(), is(1L));
        assertThat(companies.getContent().size(), is(1));
        assertThat(companies.getNumberOfElements(), is(1));
        assertThat(companies.getContent().getFirst().id(), is(companiesResponse.id()));
        assertThat(companies.getContent().getFirst().name(), is(companiesResponse.name()));
        assertThat(companies.getContent().getFirst().cnpj(), is(companiesResponse.cnpj()));
        assertThat(companies.getContent().getFirst().broker(), is(companiesResponse.broker()));
        assertThat(companies.getContent().getFirst().listed(), is(companiesResponse.listed()));
    }

    @Test
    public void getByCompanyCnpj() {
        // given - precondition or setup
        var companiesResponse = getCompaniesResponse();
        var page = new PageImpl<>(List.of(companiesResponse));
        when(repository.findAllByIdOrCnpj(any(), anyString(), any(Pageable.class))).thenReturn(page);

        // when - action or the behaviour that we are going test
        var companies = service.getCompanies(null, companiesResponse.cnpj(), Pageable.unpaged());

        // then
        verify(repository, times(1)).findAllByIdOrCnpj(any(), anyString(), any(Pageable.class));
        assertThat(companies, notNullValue());
        assertThat(companies.getTotalPages(), is(1));
        assertThat(companies.getTotalElements(), is(1L));
        assertThat(companies.getContent().size(), is(1));
        assertThat(companies.getNumberOfElements(), is(1));
        assertThat(companies.getContent().getFirst().id(), is(companiesResponse.id()));
        assertThat(companies.getContent().getFirst().name(), is(companiesResponse.name()));
        assertThat(companies.getContent().getFirst().cnpj(), is(companiesResponse.cnpj()));
        assertThat(companies.getContent().getFirst().broker(), is(companiesResponse.broker()));
        assertThat(companies.getContent().getFirst().listed(), is(companiesResponse.listed()));
    }

    @Test
    public void createCompany() {
        // given - precondition or setup
        when(repository.existsByCnpj(anyString())).thenReturn(false);

        // when - action or the behaviour that we are going test
        service.createCompanies(getCompaniesRequest());

        // then
        verify(repository, times(1)).save(any(CompaniesEntity.class));
    }

    @Test
    public void failToCreateCompany() {
        // given - precondition or setup
        when(repository.existsByCnpj(anyString())).thenReturn(true);

        // when - action or the behaviour that we are going test
        assertThrows(CompaniesAlreadyExistsException.class, () -> service.createCompanies(getCompaniesRequest()));

        // then
        verify(repository, never()).save(any(CompaniesEntity.class));
    }

    @Test
    public void updateCompany() {
        // given - precondition or setup
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(getCompaniesEntity()));

        // when - action or the behaviour that we are going test
        service.updateCompanies(UUID.randomUUID(), getCompaniesRequest());

        // then
        verify(repository, never()).delete(any(CompaniesEntity.class));
    }

    @Test
    public void failToUpdateCompany() {
        // given - precondition or setup
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        assertThrows(CompaniesNotFoundException.class, () -> service.updateCompanies(UUID.randomUUID(), getCompaniesRequest()));

        // then
        verify(repository, never()).delete(any(CompaniesEntity.class));
    }

    @Test
    public void deleteCompany() {
        // given - precondition or setup
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(getCompaniesEntity()));

        // when - action or the behaviour that we are going test
        service.deleteCompanies(UUID.randomUUID());

        // then
        verify(repository, times(1)).delete(any(CompaniesEntity.class));
    }

    @Test
    public void failToDeleteCompany() {
        // given - precondition or setup
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        assertThrows(CompaniesNotFoundException.class, () -> service.deleteCompanies(UUID.randomUUID()));

        // then
        verify(repository, never()).delete(any(CompaniesEntity.class));
    }
}