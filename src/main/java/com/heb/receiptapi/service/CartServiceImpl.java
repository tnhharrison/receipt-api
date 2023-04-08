package com.heb.receiptapi.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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

    private final BigDecimal TAX_RATE = BigDecimal.valueOf(0.0825);
    private final MathContext CONTEXT = new MathContext(2, RoundingMode.HALF_UP);

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
        BigDecimal subtotal = new BigDecimal(0.00),
            taxableSubtotal = new BigDecimal(0.00);
        for (var item : items) {
            if (item.isTaxable()) {
                taxableSubtotal = taxableSubtotal.add(item.getPrice());
            }
            subtotal = subtotal.add(item.getPrice());
        }

        BigDecimal taxTotal = taxableSubtotal.multiply(TAX_RATE);
        BigDecimal grandTotal = subtotal.add(taxTotal);

        log.info("Exiting checkout()");

        return ReceiptResponse.builder()
                .subtotal(subtotal.setScale(2, RoundingMode.HALF_UP))
                .taxableSubtotal(taxableSubtotal.setScale(2, RoundingMode.HALF_UP))
                .taxTotal(taxTotal.setScale(2, RoundingMode.HALF_UP))
                .grandTotal(grandTotal.setScale(2, RoundingMode.HALF_UP))
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
        BigDecimal subtotal = BigDecimal.valueOf(0.00),
            taxableSubtotal = BigDecimal.valueOf(0.00),
            discountTotal = BigDecimal.valueOf(0.00);

        // for (var item : items) {
        //     // check if item has discount
        //     // calc final price
        //     BigDecimal discount = coupons.getDiscount(item.getSku());
        //     discountTotal = discountTotal + discount;
        //     BigDecimal finalPrice = item.getPrice() - discount;

        //     //final price cannot be negative
        //     if(finalPrice < 0.00) {
        //         finalPrice = 0.00;
        //     }
        //     if (item.isTaxable()) {
        //         taxableSubtotal = taxableSubtotal + finalPrice;
        //     }
        //     subtotal = subtotal + item.getPrice();
        // }

        // BigDecimal subtotalAfterDiscounts = subtotal - discountTotal;

        // BigDecimal taxTotal = this.round2places(taxableSubtotal * TAX_RATE);
        // BigDecimal grandTotal = subtotal + taxTotal;

        // log.info("Exiting checkoutWithCoupons()");

        return ReceiptWithDiscountsResponse.builder()
                .subtotalBeforeDiscounts(subtotal)
                .subtotalAfterDiscounts(BigDecimal.valueOf(0.00))
                .discountTotal(discountTotal)
                .taxableSubtotalAfterDiscounts(taxableSubtotal)
                .taxTotal(BigDecimal.valueOf(0.00))
                .grandTotal(BigDecimal.valueOf(0.00))
                .build();
    }
}
