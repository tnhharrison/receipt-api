package com.heb.receiptapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.heb.receiptapi.model.Item;
import com.heb.receiptapi.model.ReceiptResponse;

public class CartServiceImplTest {
    CartServiceImpl cartServiceImpl = new CartServiceImpl();

    @Test
    void testEmptyCart() {
        ReceiptResponse expected = ReceiptResponse.builder()
                .grandTotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .subtotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .taxTotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .taxableSubtotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .build();
        ReceiptResponse actual = cartServiceImpl.checkout(List.of());
        assertEquals(actual, expected);
    }

    @Test
    void testTaxRateCalc() {
        Item lipstick = Item.builder()
                .isTaxable(true)
                .itemName("Lipstick")
                .ownBrand(false)
                .sku(123456)
                .price(BigDecimal.valueOf(3.99))
                .build();
        Item pastaSauce = Item.builder()
                .isTaxable(true)
                .itemName("Central Market Pasta Sauce")
                .ownBrand(true)
                .sku(223344)
                .price(BigDecimal.valueOf(3.99))
                .build();
        List<Item> items = List.of(lipstick, pastaSauce);

        var subtotal = BigDecimal.valueOf(7.98);
        var taxableSubtotal = BigDecimal.valueOf(7.98);
        var taxTotal = BigDecimal.valueOf(0.66);
        var grandTotal = BigDecimal.valueOf(8.64);

        ReceiptResponse expected = ReceiptResponse.builder()
                .grandTotal(grandTotal)
                .subtotal(subtotal)
                .taxTotal(taxTotal)
                .taxableSubtotal(taxableSubtotal)
                .build();
        ReceiptResponse actual = cartServiceImpl.checkout(items);
        assertEquals(actual, expected);
    }

    @Test
    void testIsTaxableCalc() {
        Item lipstick = Item.builder()
                .isTaxable(true)
                .itemName("Lipstick")
                .ownBrand(false)
                .sku(123456)
                .price(BigDecimal.valueOf(3.99))
                .build();
        Item cucumber = Item.builder()
                .isTaxable(false)
                .itemName("Cucumber")
                .ownBrand(false)
                .sku(654321)
                .price(BigDecimal.valueOf(0.99))
                .build();
        Item pastaSauce = Item.builder()
                .isTaxable(true)
                .itemName("Central Market Pasta Sauce")
                .ownBrand(true)
                .sku(223344)
                .price(BigDecimal.valueOf(3.99))
                .build();
        List<Item> items = List.of(lipstick, cucumber, pastaSauce);

        var subtotal = BigDecimal.valueOf(8.97);
        var taxableSubtotal = BigDecimal.valueOf(7.98);
        var taxTotal = BigDecimal.valueOf(0.66);
        var grandTotal = BigDecimal.valueOf(9.63);
        
        ReceiptResponse expected = ReceiptResponse.builder()
                .grandTotal(grandTotal)
                .subtotal(subtotal)
                .taxTotal(taxTotal)
                .taxableSubtotal(taxableSubtotal)
                .build();
        ReceiptResponse actual = cartServiceImpl.checkout(items);
        assertEquals(actual, expected);
    }
}
