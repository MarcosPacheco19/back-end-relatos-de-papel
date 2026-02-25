package com.relatospapel.ms_books_payments.service.impl;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import com.relatospapel.ms_books_payments.enums.OrderStatus;
import com.relatospapel.ms_books_payments.enums.BookFormat;
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
        CustomerEntity customer = customerRepository.findByEmail(req.getBuyerEmail())
                .orElseGet(() -> {
                    CustomerEntity newCustomer = CustomerEntity.builder()
                            .email(req.getBuyerEmail())
                            .build();
                    return customerRepository.save(newCustomer);
                });
        List<PurchaseResponse.Item> responseItems = new ArrayList<>();
        List<OrderItemEntity> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PurchaseCreateRequest.Item item : req.getItems()) {
            BookAvailabilityResponse availability;
            try {
                availability = catalogueClient.availability(item.getBookId(), item.getQuantity());
            } catch (FeignException.NotFound e) {
                log.error("Libro no encontrado en el catálogo: {}", item.getBookId(), e);
                throw new BookNotFoundException("Libro no encontrado: " + item.getBookId());
            } catch (FeignException.Conflict e) {
                log.error("Conflicto al validar disponibilidad del libro: {}", item.getBookId(), e);
                throw new InsufficientStockException("Libro no disponible: " + item.getBookId());
            } catch (FeignException.ServiceUnavailable | FeignException.InternalServerError e) {
                log.error("Servicio de catálogo no disponible", e);
                throw new UpstreamServiceException("El servicio de catálogo está temporalmente no disponible. Por favor, inténtelo más tarde.");
            } catch (FeignException e) {
                log.error("Error al comunicarse con el servicio de catálogo", e);
                throw new UpstreamServiceException("Error al validar la disponibilidad del libro. Por favor, inténtelo más tarde.");
            }
            if (!availability.isExists()) {
                throw new BookNotFoundException("Libro no encontrado: " + item.getBookId());
            }
            if (!availability.isAvailable()) {
                throw new InsufficientStockException("Libro no disponible: " + item.getBookId() +
                        ". Razón: " + availability.getReason());
            }
            BigDecimal unitPrice = new BigDecimal("29.99");
            BigDecimal lineTotal = unitPrice.multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);
            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .bookIdRef(item.getBookId())
                    .isbnRef("N/D")
                    .titleSnapshot("Instantánea del título del libro")
                    .formatSnapshot(BookFormat.PHYSICAL)
                    .quantity(item.getQuantity())
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .build();
            orderItems.add(orderItem);
            responseItems.add(PurchaseResponse.Item.builder()
                    .bookId(item.getBookId())
                    .quantity(item.getQuantity())
                    .validationReason("OK")
                    .build());
        }
        OrderEntity order = OrderEntity.builder()
                .customer(customer)
                .status(OrderStatus.CREATED)
                .totalAmount(totalAmount)
                .currency("USD")
                .build();
        for (OrderItemEntity item : orderItems) {
            order.addItem(item);
        }
        order = orderRepository.save(order);
        return PurchaseResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .createdAt(ZonedDateTime.of(order.getCreatedAt(), ZoneId.systemDefault()).toInstant())
                .items(responseItems)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getById(UUID id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada: " + id));

        List<PurchaseResponse.Item> items = order.getItems().stream()
                .map(item -> PurchaseResponse.Item.builder()
                        .bookId(item.getBookIdRef())
                        .quantity(item.getQuantity())
                        .validationReason("N/D")
                        .build())
                .toList();

        return PurchaseResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .createdAt(ZonedDateTime.of(order.getCreatedAt(), ZoneId.systemDefault()).toInstant())
                .items(items)
                .build();
    }
}
