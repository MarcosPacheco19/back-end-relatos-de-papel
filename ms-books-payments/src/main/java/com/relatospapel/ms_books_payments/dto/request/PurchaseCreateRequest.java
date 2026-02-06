package com.relatospapel.ms_books_payments.dto.request;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para crear una compra")
public class PurchaseCreateRequest {

    @NotBlank(message = "El email del comprador es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    @Schema(description = "Email del comprador", example = "buyer@example.com")
    private String buyerEmail;

    @NotEmpty(message = "La lista de items no puede estar vacia")
    @Valid
    @Schema(description = "Lista de items a comprar", required = true)
    private List<Item> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Item de la compra")
    public static class Item {

        @NotNull(message = "El ID del libro es obligatorio")
        @Schema(description = "Identificador del libro", required = true)
        private UUID bookId;

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        @Schema(description = "Cantidad a comprar", example = "1")
        private Integer quantity;
    }
}
