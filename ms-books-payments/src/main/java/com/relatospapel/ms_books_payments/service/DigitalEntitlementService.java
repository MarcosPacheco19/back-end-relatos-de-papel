package com.relatospapel.ms_books_payments.service;

import java.util.List;
import java.util.UUID;

import com.relatospapel.ms_books_payments.dto.request.DigitalEntitlementCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.DigitalEntitlementResponse;

/**
 * Servicio para la gestión de entregas digitales.
 * Define las operaciones para crear, activar y consultar entregas digitales de libros.
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
public interface DigitalEntitlementService {

    /**
     * Crea una nueva entrega digital.
     * 
     * @param req datos de la entrega digital a crear
     * @return información de la entrega digital creada
     */
    DigitalEntitlementResponse create(DigitalEntitlementCreateRequest req);

    /**
     * Activa una entrega digital existente.
     * 
     * @param entitlementId identificador de la entrega digital
     * @return información de la entrega digital activada
     */
    DigitalEntitlementResponse activate(UUID entitlementId);

    /**
     * Lista las entregas digitales de una orden específica.
     * 
     * @param orderId identificador de la orden
     * @return lista de entregas digitales
     */
    List<DigitalEntitlementResponse> listByOrder(UUID orderId);

    /**
     * Lista las entregas digitales de un cliente específico.
     * 
     * @param customerId identificador del cliente
     * @return lista de entregas digitales
     */
    List<DigitalEntitlementResponse> listByCustomer(UUID customerId);
}
