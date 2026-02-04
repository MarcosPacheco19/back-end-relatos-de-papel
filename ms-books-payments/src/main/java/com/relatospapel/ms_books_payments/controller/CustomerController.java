package com.relatospapel.ms_books_payments.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.relatospapel.ms_books_payments.dto.request.CustomerCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.CustomerResponse;
import com.relatospapel.ms_books_payments.entity.CustomerEntity;
import com.relatospapel.ms_books_payments.exception.NotFoundException;
import com.relatospapel.ms_books_payments.repository.CustomerRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CustomerCreateRequest req) {
        CustomerEntity customer = CustomerEntity.builder()
                .email(req.getEmail())
                .preferredLanguage(req.getPreferredLanguage())
                .build();
        
        customer = customerRepository.save(customer);
        
        return new CustomerResponse(
                customer.getId(),
                customer.getEmail(),
                customer.getPreferredLanguage(),
                customer.getCreatedAt()
        );
    }

    @GetMapping("/{id}")
    public CustomerResponse getById(@PathVariable UUID id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer no encontrado con ID: " + id));
        
        return new CustomerResponse(
                customer.getId(),
                customer.getEmail(),
                customer.getPreferredLanguage(),
                customer.getCreatedAt()
        );
    }
}
