package com.relatospapel.ms_books_payments.service;

import java.util.List;
import java.util.UUID;

import com.relatospapel.ms_books_payments.dto.request.OrderItemCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.OrderItemResponse;

/**
 * Servicio para la gestión de items de orden.
 * Define las operaciones para crear y consultar items de órdenes.
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
public interface OrderItemService {

    /**
     * Crea un nuevo item de orden.
     * 
     * @param req datos del item a crear
     * @return información del item creado
     */
    OrderItemResponse create(OrderItemCreateRequest req);

    /**
     * Lista los items de una orden específica.
     * 
     * @param orderId identificador de la orden
     * @return lista de items de la orden
     */
    List<OrderItemResponse> listByOrder(UUID orderId);
}
