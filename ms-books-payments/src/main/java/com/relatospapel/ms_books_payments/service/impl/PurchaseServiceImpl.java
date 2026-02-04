package com.relatospapel.ms_books_payments.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.relatospapel.ms_books_payments.client.CatalogueClient;
import com.relatospapel.ms_books_payments.dto.request.PurchaseCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.BookAvailabilityResponse;
import com.relatospapel.ms_books_payments.dto.response.PurchaseResponse;
import com.relatospapel.ms_books_payments.entity.CustomerEntity;
import com.relatospapel.ms_books_payments.entity.OrderEntity;
import com.relatospapel.ms_books_payments.entity.OrderItemEntity;
import com.relatospapel.ms_books_payments.entity.enums.OrderStatus;
import com.relatospapel.ms_books_payments.exception.BookNotFoundException;
import com.relatospapel.ms_books_payments.exception.InsufficientStockException;
import com.relatospapel.ms_books_payments.exception.NotFoundException;
import com.relatospapel.ms_books_payments.exception.UpstreamServiceException;
import com.relatospapel.ms_books_payments.repository.CustomerRepository;
import com.relatospapel.ms_books_payments.repository.OrderRepository;
import com.relatospapel.ms_books_payments.service.PurchaseService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CatalogueClient catalogueClient;

    @Override
    public PurchaseResponse create(PurchaseCreateRequest req) {
        // 1. Buscar o crear cliente
        CustomerEntity customer = customerRepository.findByEmail(req.getBuyerEmail())
                .orElseGet(() -> {
                    CustomerEntity newCustomer = CustomerEntity.builder()
                            .email(req.getBuyerEmail())
                            .build();
                    return customerRepository.save(newCustomer);
                });

        // 2. Validar cada item con el catalogo
        List<PurchaseResponse.Item> responseItems = new ArrayList<>();
        List<OrderItemEntity> orderItems = new ArrayList<>();
        java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;

        for (PurchaseCreateRequest.Item item : req.getItems()) {
            BookAvailabilityResponse availability;
            
            try {
                availability = catalogueClient.availability(item.getBookId(), item.getQuantity());
            } catch (FeignException.NotFound e) {
                log.error("Book not found in catalogue: {}", item.getBookId(), e);
                throw new BookNotFoundException("Book not found: " + item.getBookId());
            } catch (FeignException.Conflict e) {
                log.error("Conflict validating book availability: {}", item.getBookId(), e);
                throw new InsufficientStockException("Book not available: " + item.getBookId());
            } catch (FeignException.ServiceUnavailable | FeignException.InternalServerError e) {
                log.error("Catalogue service unavailable", e);
                throw new UpstreamServiceException("Catalogue service is temporarily unavailable. Please try again later.");
            } catch (FeignException e) {
                log.error("Error communicating with catalogue service", e);
                throw new UpstreamServiceException("Error validating book availability. Please try again later.");
            }

            // Validar que el libro existe
            if (!availability.isExists()) {
                throw new BookNotFoundException("Book not found: " + item.getBookId());
            }

            // Validar que el libro esta disponible
            if (!availability.isAvailable()) {
                throw new InsufficientStockException("Book not available: " + item.getBookId() +
                        ". Reason: " + availability.getReason());
            }

            // Calcular precios (precio hardcoded temporalmente - idealmente viene del catalogo)
            java.math.BigDecimal unitPrice = new java.math.BigDecimal("29.99");
            java.math.BigDecimal lineTotal = unitPrice.multiply(new java.math.BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);

            // Crear item de orden con snapshot de datos
            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .bookIdRef(item.getBookId())
                    .isbnRef("N/A")
                    .titleSnapshot("Book Title Snapshot")
                    .imageUrlSnapshot("PHYSICAL")
                    .quantity(item.getQuantity())
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .build();
            orderItems.add(orderItem);

            // Crear item de respuesta
            responseItems.add(PurchaseResponse.Item.builder()
                    .bookId(item.getBookId())
                    .quantity(item.getQuantity())
                    .validationReason("OK")
                    .build());
        }

        // 3. Crear orden
        OrderEntity order = OrderEntity.builder()
                .customer(customer)
                .status(OrderStatus.CREATED)
                .totalAmount(totalAmount)
                .currency("USD")
                .build();

        // 4. Asociar items a la orden
        for (OrderItemEntity item : orderItems) {
            order.addItem(item);
        }

        // 5. Guardar orden con cascade
        order = orderRepository.save(order);

        // 6. Retornar respuesta
        return PurchaseResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .items(responseItems)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getById(UUID id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));

        List<PurchaseResponse.Item> items = order.getItems().stream()
                .map(item -> PurchaseResponse.Item.builder()
                        .bookId(item.getBookIdRef())
                        .quantity(item.getQuantity())
                        .validationReason("N/A")
                        .build())
                .toList();

        return PurchaseResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}
