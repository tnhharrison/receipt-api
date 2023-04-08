package com.heb.receiptapi.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

/**
 * Payload returned from the v1 receipt API
 */
@Data
@Builder
public class ReceiptResponse {
    private BigDecimal grandTotal;
    private BigDecimal subtotal;
    private BigDecimal taxableSubtotal;
    private BigDecimal taxTotal;
}
