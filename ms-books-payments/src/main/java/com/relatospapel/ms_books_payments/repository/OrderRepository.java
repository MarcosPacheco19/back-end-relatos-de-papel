package com.relatospapel.ms_books_payments.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.relatospapel.ms_books_payments.entity.OrderEntity;
import com.relatospapel.ms_books_payments.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findByCustomerId(UUID customerId);
    
    List<OrderEntity> findByStatus(OrderStatus status);
    
    List<OrderEntity> findByCustomerIdAndStatus(UUID customerId, OrderStatus status);
}
