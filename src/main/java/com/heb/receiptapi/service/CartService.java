package com.heb.receiptapi.service;

import java.util.List;

import com.heb.receiptapi.model.Item;
import com.heb.receiptapi.model.ReceiptResponse;
import com.heb.receiptapi.model.ReceiptWithDiscountsResponse;

/**
 * Service to hold the business logic for fufilling grocery carts
 */
public interface CartService {
    /**
     * business logic for checking out a list of grocery items, handles taxable & nontaxable items
     * @param items
     * @return
     */
    public abstract ReceiptResponse checkout(List<Item> items);

    /**
     * business logic for checking out a list of grocery item, handles taxable & nontaxable items, also handles discount items
     * @param items
     * @return
     */
    public abstract ReceiptWithDiscountsResponse checkoutWithCoupons(List<Item> items);
}
