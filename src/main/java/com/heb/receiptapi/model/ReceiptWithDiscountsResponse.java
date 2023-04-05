package com.heb.receiptapi.model;

import lombok.Builder;
import lombok.Data;

/**
 * Payload returned from the v2 Receipt API
 */
@Data
@Builder
public class ReceiptWithDiscountsResponse {
    private float subtotalBeforeDiscounts;
    private float discountTotal;
    private float subtotalAfterDiscounts;
    private float taxableSubtotalAfterDiscounts;
    private float taxTotal;
    private float grandTotal;
}
