package com.heb.receiptapi.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

/**
 * Payload returned from the v2 Receipt API
 */
@Data
@Builder
public class ReceiptWithDiscountsResponse {
    private BigDecimal subtotalBeforeDiscounts;
    private BigDecimal discountTotal;
    private BigDecimal subtotalAfterDiscounts;
    private BigDecimal taxableSubtotalAfterDiscounts;
    private BigDecimal taxTotal;
    private BigDecimal grandTotal;
}
