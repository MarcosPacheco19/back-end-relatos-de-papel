package com.relatospapel.ms_books_payments.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.relatospapel.ms_books_payments.dto.request.PaymentCaptureRequest;
import com.relatospapel.ms_books_payments.dto.request.PaymentCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.PaymentResponse;
import com.relatospapel.ms_books_payments.entity.DigitalEntitlementEntity;
import com.relatospapel.ms_books_payments.entity.OrderEntity;
import com.relatospapel.ms_books_payments.entity.OrderItemEntity;
import com.relatospapel.ms_books_payments.entity.PaymentEntity;
import com.relatospapel.ms_books_payments.enums.BookFormat;
import com.relatospapel.ms_books_payments.enums.OrderStatus;
import com.relatospapel.ms_books_payments.enums.PaymentStatus;
import com.relatospapel.ms_books_payments.exception.NotFoundException;
import com.relatospapel.ms_books_payments.repository.DigitalEntitlementRepository;
import com.relatospapel.ms_books_payments.repository.OrderItemRepository;
import com.relatospapel.ms_books_payments.repository.OrderRepository;
import com.relatospapel.ms_books_payments.repository.PaymentRepository;
import com.relatospapel.ms_books_payments.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepo;
  private final OrderRepository orderRepo;
  private final OrderItemRepository orderItemRepo;
  private final DigitalEntitlementRepository entitlementRepo;

  @Override
  public PaymentResponse create(PaymentCreateRequest req) {

    OrderEntity order = orderRepo.findById(req.getOrderId())
        .orElseThrow(() -> new NotFoundException("Orden no encontrada"));

    PaymentEntity payment = PaymentEntity.builder()
        .orderId(order.getId())
        .method(req.getMethod())
        .status(PaymentStatus.PENDING)
        .amount(order.getTotalAmount())
        .currency(order.getCurrency())
        .build();

    payment = paymentRepo.save(payment);

    return PaymentResponse.from(payment);
  }

  @Override
  public PaymentResponse capture(UUID paymentId, PaymentCaptureRequest req) {

    PaymentEntity payment = paymentRepo.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

    payment.setStatus(PaymentStatus.CAPTURED);
    payment.setProviderTxId(req.getProviderTxId());

    OrderEntity order = orderRepo.findById(payment.getOrderId())
        .orElseThrow(() -> new NotFoundException("Orden no encontrada"));

    order.setStatus(OrderStatus.PAID);

    // Generar entitlements digitales
    List<OrderItemEntity> items = orderItemRepo.findByOrderId(order.getId());

    items.stream()
        .filter(i -> i.getFormatSnapshot() == BookFormat.DIGITAL)
        .forEach(i -> {
          DigitalEntitlementEntity e = DigitalEntitlementEntity.builder()
              .orderItemId(i.getId())
              .deliveryType(req.getDeliveryType())
              .resourceRef(req.getResourceRef())
              .build();
          entitlementRepo.save(e);
        });

    return PaymentResponse.from(payment);
  }

  @Override
  public PaymentResponse fail(UUID paymentId) {

    PaymentEntity payment = paymentRepo.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

    payment.setStatus(PaymentStatus.FAILED);

    return PaymentResponse.from(payment);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PaymentResponse> listByOrder(UUID orderId) {

    return paymentRepo.findByOrderId(orderId).stream()
        .map(PaymentResponse::from)
        .toList();
  }
}
