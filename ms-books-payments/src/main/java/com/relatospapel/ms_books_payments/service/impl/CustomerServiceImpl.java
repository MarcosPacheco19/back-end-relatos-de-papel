package com.relatospapel.ms_books_payments.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.relatospapel.ms_books_payments.dto.request.CustomerCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.CustomerResponse;
import com.relatospapel.ms_books_payments.entity.CustomerEntity;
import com.relatospapel.ms_books_payments.exception.NotFoundException;
import com.relatospapel.ms_books_payments.repository.CustomerRepository;
import com.relatospapel.ms_books_payments.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    @Override
    public CustomerResponse create(CustomerCreateRequest request) {
        // Validar que no exista el email
        //repo.findByEmail(request.getEmail()).ifPresent(c -> {
        //    throw new IllegalArgumentException("El email ya existe");
        //});

        CustomerEntity entity = CustomerEntity.builder()
                .email(request.getEmail())
                .preferredLanguage(request.getPreferredLanguage())
                .build();

        entity = repo.save(entity);

        return CustomerResponse.from(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse get(UUID id) {
        CustomerEntity c = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return CustomerResponse.from(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> list() {
        return repo.findAll().stream()
                .map(CustomerResponse::from)
                .toList();
    }
}
