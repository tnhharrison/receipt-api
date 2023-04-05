package com.heb.receiptapi.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heb.receiptapi.model.Coupon;

import lombok.extern.java.Log;

/**
 * Coupon Repository - reads coupons from a json file in the resources folder
 */
@Repository
@Log
public class CouponRepoImpl implements CouponRepo {

    @Value("${couponFilePath}")
    private String couponFilePath;
    private ObjectMapper mapper = new ObjectMapper();
    private List<Coupon> couponList;

    /**
     * Function to search through coupons for the item's sku and return the discount price
     * @param sku
     * @returns
     */
    @Override
    public float getDiscount(long sku) {
        log.info("entering getDiscount()");

        try {
            // long term this isn't a great solution because it reads the entire list of coupons on each lookup
            couponList = mapper.readValue(new File(this.couponFilePath), new TypeReference<List<Coupon>>(){});
        } catch (IOException e) {
            log.warning("No coupons processed for this transaction! - Error occurred when trying to read coupons from file " + e.getMessage());
            // set list to empty
            couponList = List.of();
        }
        for(Coupon c : couponList) {
            if(c.getAppliedSku() == sku) {
                log.info("exiting getDiscount() -- found discount!");
                return c.getDiscountPrice();
            }
        }
        log.info("exiting getDiscount() -- no discount");
        return 0;
    }
    
}
