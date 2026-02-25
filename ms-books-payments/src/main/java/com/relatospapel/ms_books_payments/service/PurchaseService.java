package com.relatospapel.ms_books_payments.service;

import java.util.UUID;

import com.relatospapel.ms_books_payments.dto.request.PurchaseCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.PurchaseResponse;

public interface PurchaseService {

    PurchaseResponse create(PurchaseCreateRequest req);

    PurchaseResponse getById(UUID id);
}
