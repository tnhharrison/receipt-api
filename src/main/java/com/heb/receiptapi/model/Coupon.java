package com.heb.receiptapi.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * Class describing information about a coupon
 */
@Data
@Value
@Jacksonized
@Builder
public class Coupon {
    private String couponName;
    private long appliedSku;
    private float discountPrice;
}
