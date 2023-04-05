package com.heb.receiptapi.model;

import lombok.Builder;
import lombok.Data;

/**
 * Payload returned from the v1 receipt API
 */
@Data
@Builder
public class ReceiptResponse {
    private float grandTotal;
    private float subtotal;
    private float taxableSubtotal;
    private float taxTotal;
}
