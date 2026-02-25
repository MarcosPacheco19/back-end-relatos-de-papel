package com.relatospapel.ms_books_payments.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.relatospapel.ms_books_payments.entity.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

    List<OrderItemEntity> findByOrderId(UUID orderId);
    
    List<OrderItemEntity> findByOrderIdIn(List<UUID> orderIds);
    
    List<OrderItemEntity> findByBookIdRef(UUID bookIdRef);
    
    List<OrderItemEntity> findByIsbnRef(String isbnRef);
}
