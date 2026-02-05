package com.relatospapel.ms_books_payments.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.relatospapel.ms_books_payments.dto.request.OrderItemCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.OrderItemResponse;
import com.relatospapel.ms_books_payments.entity.OrderItemEntity;
import com.relatospapel.ms_books_payments.repository.OrderItemRepository;
import com.relatospapel.ms_books_payments.service.OrderItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepo;

    @Override
    public OrderItemResponse create(OrderItemCreateRequest req) {
        OrderItemEntity entity = OrderItemEntity.builder()
                .orderId(req.getOrderId())
                .bookIdRef(req.getBookIdRef())
                .isbnRef(req.getIsbnRef())
                .titleSnapshot(req.getTitleSnapshot())
                .formatSnapshot(req.getFormatSnapshot())
                .quantity(req.getQuantity())
                .unitPrice(req.getUnitPrice())
                .build();

        entity = orderItemRepo.save(entity);

        return OrderItemResponse.from(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponse> listByOrder(UUID orderId) {
        return orderItemRepo.findByOrderId(orderId).stream()
                .map(OrderItemResponse::from)
                .toList();
    }
}
