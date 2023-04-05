package com.heb.receiptapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heb.receiptapi.model.Cart;
import com.heb.receiptapi.model.ReceiptResponse;
import com.heb.receiptapi.model.ReceiptWithDiscountsResponse;
import com.heb.receiptapi.service.CartService;

import lombok.extern.java.Log;

/**
 * Controller for the Receipt API
 */
@RestController
@Log
public class ReceiptController {

    @Autowired
    CartService cartService;

    /**
     * Version 1 of the receipt api - this processes the isTaxable field on the grocery item
     * @param cart - cart full of groceries
     * @return - receipt describing grand total, subtotal and tax totals
     */
    @PostMapping("/v1/checkout")
    public ResponseEntity<ReceiptResponse> checkoutV1(@RequestBody Cart cart) {
        log.info("Received receipt v1 request");
        ReceiptResponse receipt = this.cartService.checkout(cart.getItems());
        log.info("Completed processing receipt v1");
        return ResponseEntity.ok(receipt);
    }

    /**
     * Version 2 of the receipt api to handle processing coupons, the response from the API was different enough
     * that I decided to split it out into a different version.
     * @param cart - cart full of groceries
     * @return - receipt describing grandTotal, subtotal, tax subtotals before and after taxes, and discounts totals
     */
    @PostMapping("/v2/checkout")
    public ResponseEntity<ReceiptWithDiscountsResponse> checkoutV2(@RequestBody Cart cart) {
        log.info("Received receipt v2 request");
        ReceiptWithDiscountsResponse receipt = this.cartService.checkoutWithCoupons(cart.getItems());
        log.info("Completed processing receipt v2");
        return ResponseEntity.ok(receipt);
    }
}