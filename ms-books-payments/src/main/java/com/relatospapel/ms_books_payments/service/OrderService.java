package com.relatospapel.ms_books_payments.service;

import java.util.List;
import java.util.UUID;

import com.relatospapel.ms_books_payments.dto.request.OrderCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.OrderResponse;

/**
 * Servicio para la gestión de órdenes.
 * Define las operaciones para crear y consultar órdenes de compra.
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
public interface OrderService {

    /**
     * Crea una nueva orden de compra.
     * 
     * @param req datos de la orden a crear
     * @return información de la orden creada
     */
    OrderResponse create(OrderCreateRequest req);

    /**
     * Lista las órdenes de un cliente específico.
     * 
     * @param customerId identificador del cliente
     * @return lista de órdenes del cliente
     */
    List<OrderResponse> listByCustomer(UUID customerId);

    /**
     * Obtiene una orden por su ID.
     * 
     * @param id identificador de la orden
     * @return información de la orden
     */
    OrderResponse get(UUID id);
}
