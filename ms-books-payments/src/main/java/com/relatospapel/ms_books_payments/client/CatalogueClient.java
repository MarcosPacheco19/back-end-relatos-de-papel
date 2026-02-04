package com.relatospapel.ms_books_payments.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.relatospapel.ms_books_payments.dto.response.BookAvailabilityResponse;

@FeignClient(name = "ms-books-catalogue")
public interface CatalogueClient {

    @GetMapping("/api/v1/catalogue/books/{id}/availability")
    BookAvailabilityResponse availability(
            @PathVariable("id") UUID id,
            @RequestParam("quantity") int quantity);
}
