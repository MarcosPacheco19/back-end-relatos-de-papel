package com.relatospapel.ms_books_payments.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.relatospapel.ms_books_payments.enums.DeliveryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "digital_entitlements")
public class DigitalEntitlementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @EqualsAndHashCode.Include
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_item_id", nullable = false,
              foreignKey = @ForeignKey(name = "fk_digital_entitlement_order_item"))
  @ToString.Exclude
  private OrderItemEntity orderItem;

  @Column(name = "order_item_id", insertable = false, updatable = false)
  private UUID orderItemId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private DeliveryType deliveryType;

  @Column(nullable = false, length = 255)
  private String resourceRef;

  @Column
  private LocalDateTime activatedAt;
}
