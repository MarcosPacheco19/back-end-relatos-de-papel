package com.relatospapel.ms_books_payments.service;

import java.util.UUID;

import com.relatospapel.ms_books_payments.dto.request.CustomerCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.CustomerResponse;

/**
 * Servicio para la gestión de clientes.
 * Define las operaciones de creación y consulta de clientes.
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
public interface CustomerService {
    
    /**
     * Crea un nuevo cliente en el sistema.
     * 
     * @param req datos del cliente a crear
     * @return información del cliente creado
     */
    CustomerResponse create(CustomerCreateRequest req);
    
    /**
     * Obtiene un cliente por su ID.
     * 
     * @param id identificador único del cliente
     * @return información del cliente
     * @throws com.relatospapel.ms_books_payments.exception.NotFoundException si el cliente no existe
     */
    CustomerResponse getById(UUID id);
}
