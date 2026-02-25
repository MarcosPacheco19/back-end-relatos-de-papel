package com.relatospapel.ms_books_payments.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.relatospapel.ms_books_payments.enums.BookFormat;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
@Table(name = "order_items")
public class OrderItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @EqualsAndHashCode.Include
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false,
              foreignKey = @ForeignKey(name = "fk_order_item_order"))
  @ToString.Exclude
  private OrderEntity order;

  @Column(name = "order_id", insertable = false, updatable = false)
  private UUID orderId;

  @Column(nullable = false, length = 20)
  private String isbnRef;

  @Column(nullable = false, length = 200)
  private String titleSnapshot;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private BookFormat formatSnapshot;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal unitPrice;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal lineTotal;

  @Column(nullable = false)
  private UUID bookIdRef;

  @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @ToString.Exclude
  private List<DigitalEntitlementEntity> digitalEntitlements = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    if (this.lineTotal == null && this.unitPrice != null) {
      this.lineTotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
  }

  public void addDigitalEntitlement(DigitalEntitlementEntity entitlement) {
    digitalEntitlements.add(entitlement);
    entitlement.setOrderItem(this);
  }
}
