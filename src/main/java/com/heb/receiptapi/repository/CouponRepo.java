package com.heb.receiptapi.repository;

import java.math.BigDecimal;

/**
 * Interface for the Coupon Repository
 */
public interface CouponRepo {
    /**
     * Function to return the discounted price for a given sku
     * @param sku
     * @return
     */
    public BigDecimal getDiscount(long sku);
}
