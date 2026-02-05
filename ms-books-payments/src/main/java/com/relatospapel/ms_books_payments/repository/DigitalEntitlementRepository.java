package com.relatospapel.ms_books_payments.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.relatospapel.ms_books_payments.entity.DigitalEntitlementEntity;
import com.relatospapel.ms_books_payments.enums.DeliveryType;

@Repository
public interface DigitalEntitlementRepository extends JpaRepository<DigitalEntitlementEntity, UUID> {

    List<DigitalEntitlementEntity> findByOrderItemId(UUID orderItemId);
    
    List<DigitalEntitlementEntity> findByOrderItemIdIn(List<UUID> orderItemIds);
    
    List<DigitalEntitlementEntity> findByDeliveryType(DeliveryType deliveryType);
    
    List<DigitalEntitlementEntity> findByActivatedAtIsNull();
    
    List<DigitalEntitlementEntity> findByActivatedAtIsNotNull();
}
