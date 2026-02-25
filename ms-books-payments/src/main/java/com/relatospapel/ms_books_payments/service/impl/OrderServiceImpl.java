package com.relatospapel.ms_books_payments.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.relatospapel.ms_books_payments.dto.request.OrderCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.OrderResponse;
import com.relatospapel.ms_books_payments.entity.OrderEntity;
import com.relatospapel.ms_books_payments.enums.OrderStatus;
import com.relatospapel.ms_books_payments.exception.NotFoundException;
import com.relatospapel.ms_books_payments.repository.OrderRepository;
import com.relatospapel.ms_books_payments.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;

    @Override
    public OrderResponse create(OrderCreateRequest req) {
        
        OrderEntity order = OrderEntity.builder()
                .customerId(req.getCustomerId())
                .status(OrderStatus.CREATED)
                .totalAmount(req.getTotalAmount())
                .currency(req.getCurrency())
                .couponCodeApplied(req.getCouponCodeApplied())
                .build();

        order = orderRepo.save(order);

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .couponCodeApplied(order.getCouponCodeApplied())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> listByCustomer(UUID customerId) {
        return orderRepo.findByCustomerId(customerId).stream().map(order -> 
            OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .couponCodeApplied(order.getCouponCodeApplied())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build()
        ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse get(UUID id) {
        OrderEntity order = orderRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada"));
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .couponCodeApplied(order.getCouponCodeApplied())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
