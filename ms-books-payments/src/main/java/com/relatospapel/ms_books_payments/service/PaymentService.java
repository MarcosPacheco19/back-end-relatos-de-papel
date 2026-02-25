package com.relatospapel.ms_books_payments.service;

import java.util.List;
import java.util.UUID;

import com.relatospapel.ms_books_payments.dto.request.PaymentCaptureRequest;
import com.relatospapel.ms_books_payments.dto.request.PaymentCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.PaymentResponse;

/**
 * Servicio para la gestión de pagos.
 * Define las operaciones para crear, capturar y consultar pagos.
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
public interface PaymentService {

    /**
     * Crea un nuevo pago.
     * 
     * @param req datos del pago a crear
     * @return información del pago creado
     */
    PaymentResponse create(PaymentCreateRequest req);

    /**
     * Captura un pago previamente autorizado.
     * 
     * @param paymentId identificador del pago
     * @param req datos para la captura del pago
     * @return información del pago capturado
     */
    PaymentResponse capture(UUID paymentId, PaymentCaptureRequest req);

    /**
     * Marca un pago como fallido.
     * 
     * @param paymentId identificador del pago
     * @return información del pago marcado como fallido
     */
    PaymentResponse fail(UUID paymentId);

    /**
     * Lista los pagos de una orden específica.
     * 
     * @param orderId identificador de la orden
     * @return lista de pagos de la orden
     */
    List<PaymentResponse> listByOrder(UUID orderId);
}
