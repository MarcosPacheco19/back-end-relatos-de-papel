package com.relatospapel.ms_books_payments.service.impl;

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

/**
 * Implementación del servicio de gestión de clientes.
 * Maneja la lógica de negocio relacionada con clientes.
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse create(CustomerCreateRequest req) {
        CustomerEntity customer = CustomerEntity.builder()
                .email(req.getEmail())
                .preferredLanguage(req.getPreferredLanguage())
                .build();
        
        customer = customerRepository.save(customer);
        
        return toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getById(UUID id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + id));
        
        return toResponse(customer);
    }

    /**
     * Convierte una entidad de cliente a un DTO de respuesta.
     * 
     * @param customer entidad de cliente
     * @return DTO de respuesta con la información del cliente
     */
    private CustomerResponse toResponse(CustomerEntity customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getEmail(),
                customer.getPreferredLanguage(),
                customer.getCreatedAt()
        );
    }
}
