package com.heb.receiptapi.repository;

/**
 * Interface for the Coupon Repository
 */
public interface CouponRepo {
    /**
     * Function to return the discounted price for a given sku
     * @param sku
     * @return
     */
    public float getDiscount(long sku);
}
