package com.relatospapel.ms_books_payments.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.relatospapel.ms_books_payments.dto.request.DigitalEntitlementCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.DigitalEntitlementResponse;
import com.relatospapel.ms_books_payments.entity.DigitalEntitlementEntity;
import com.relatospapel.ms_books_payments.entity.OrderEntity;
import com.relatospapel.ms_books_payments.entity.OrderItemEntity;
import com.relatospapel.ms_books_payments.exception.NotFoundException;
import com.relatospapel.ms_books_payments.repository.DigitalEntitlementRepository;
import com.relatospapel.ms_books_payments.repository.OrderItemRepository;
import com.relatospapel.ms_books_payments.repository.OrderRepository;
import com.relatospapel.ms_books_payments.service.DigitalEntitlementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DigitalEntitlementServiceImpl implements DigitalEntitlementService {

    private final DigitalEntitlementRepository repo;
    private final OrderItemRepository orderItemRepo;   
    private final OrderRepository orderRepo;           

    @Override
    public DigitalEntitlementResponse create(DigitalEntitlementCreateRequest req) {
        DigitalEntitlementEntity e = DigitalEntitlementEntity.builder()
                .orderItemId(req.getOrderItemId())
                .deliveryType(req.getDeliveryType())
                .resourceRef(req.getResourceRef())
                .build();

        e = repo.save(e);

        return DigitalEntitlementResponse.from(e);
    }

    @Override
    public DigitalEntitlementResponse activate(UUID entitlementId) {
        DigitalEntitlementEntity e = repo.findById(entitlementId)
                .orElseThrow(() -> new NotFoundException("Entitlement digital no encontrado"));

        e.setActivatedAt(LocalDateTime.now());

        return DigitalEntitlementResponse.from(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DigitalEntitlementResponse> listByOrder(UUID orderId) {    
        List<OrderItemEntity> items = orderItemRepo.findByOrderId(orderId);
        List<UUID> itemIds = items.stream().map(OrderItemEntity::getId).toList();
        List<DigitalEntitlementEntity> entitlements = repo.findByOrderItemIdIn(itemIds);
        return entitlements.stream()
                .map(DigitalEntitlementResponse::from)
                .toList();
    }

   @Override
    @Transactional(readOnly = true)
    public List<DigitalEntitlementResponse> listByCustomer(UUID customerId) {
        List<OrderEntity> orders = orderRepo.findByCustomerId(customerId);
        List<UUID> orderIds = orders.stream().map(OrderEntity::getId).toList();
        List<OrderItemEntity> items = orderItemRepo.findByOrderIdIn(orderIds);
        List<UUID> itemIds = items.stream().map(OrderItemEntity::getId).toList();
        List<DigitalEntitlementEntity> entitlements = repo.findByOrderItemIdIn(itemIds);
        return entitlements.stream()
                .map(DigitalEntitlementResponse::from)
                .toList();
    }
}
