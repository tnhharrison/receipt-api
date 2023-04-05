package com.heb.receiptapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heb.receiptapi.model.Item;
import com.heb.receiptapi.model.ReceiptResponse;
import com.heb.receiptapi.model.ReceiptWithDiscountsResponse;
import com.heb.receiptapi.repository.CouponRepo;

import lombok.extern.java.Log;

/**
 * Implementation of the CartService interface
 */
@Service
@Log
public class CartServiceImpl implements CartService {

    private final float TAX_RATE = 0.825f;

    @Autowired
    private CouponRepo coupons;

    /**
    * business logic for checking out a list of grocery items, handles taxable & nontaxable items
    * @param items
    * @return
    */
    @Override
    public ReceiptResponse checkout(List<Item> items) {
        log.info("Entering checkout()");
        float subtotal = 0,
            taxableSubtotal = 0;
        for (var item : items) {
            if (item.isTaxable()) {
                taxableSubtotal = this.round2places(taxableSubtotal + item.getPrice());
            }
            subtotal = this.round2places(subtotal + item.getPrice());
        }

        float taxTotal = this.round2places(taxableSubtotal * TAX_RATE);
        float grandTotal = subtotal + taxTotal;

        log.info("Exiting checkout()");

        return ReceiptResponse.builder()
                .subtotal(subtotal)
                .taxableSubtotal(taxableSubtotal)
                .taxTotal(taxTotal)
                .grandTotal(grandTotal)
                .build();
    }

    /**
     * business logic for checking out a list of grocery item, handles taxable & nontaxable items, also handles discount items
     * @param items
     * @return
     */
    @Override
    public ReceiptWithDiscountsResponse checkoutWithCoupons(List<Item> items) {
        log.info("Entering checkoutWithCoupons()");
        float subtotal = 0,
            taxableSubtotal = 0,
            discountTotal = 0;

        for (var item : items) {
            // check if item has discount
            // calc final price
            float discount = coupons.getDiscount(item.getSku());
            discountTotal = this.round2places(discountTotal + discount);
            float finalPrice = (item.getPrice() - discount);

            //final price cannot be negative
            if(finalPrice < 0) {
                finalPrice = 0;
            }
            if (item.isTaxable()) {
                taxableSubtotal = this.round2places(taxableSubtotal + finalPrice);
            }
            subtotal = this.round2places(subtotal + item.getPrice());
        }

        float subtotalAfterDiscounts = subtotal - discountTotal;

        float taxTotal = this.round2places(taxableSubtotal * TAX_RATE);
        float grandTotal = subtotal + taxTotal;

        log.info("Exiting checkoutWithCoupons()");

        return ReceiptWithDiscountsResponse.builder()
                .subtotalBeforeDiscounts(subtotal)
                .subtotalAfterDiscounts(subtotalAfterDiscounts)
                .discountTotal(discountTotal)
                .taxableSubtotalAfterDiscounts(taxableSubtotal)
                .taxTotal(taxTotal)
                .grandTotal(grandTotal)
                .build();
    }

    /**
     * Helper function to round a float to two decimal places
     */
    private float round2places(float num) {
        return Math.round((num * 100.0) / 100.0);
    }
}
