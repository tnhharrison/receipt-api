package com.heb.receiptapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.heb.receiptapi.model.Item;
import com.heb.receiptapi.model.ReceiptResponse;

public class CartServiceImplTest {
    private final float TAX_RATE = 0.825f;

    CartServiceImpl cartServiceImpl = new CartServiceImpl();

    @Test
    void testEmptyCart() {
        ReceiptResponse expected = ReceiptResponse.builder()
                .grandTotal(0)
                .subtotal(0)
                .taxTotal(0)
                .taxableSubtotal(0)
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
                .price(3.99f)
                .build();
        Item pastaSauce = Item.builder()
                .isTaxable(true)
                .itemName("Central Market Pasta Sauce")
                .ownBrand(true)
                .sku(223344)
                .price(3.99f)
                .build();
        List<Item> items = List.of(lipstick, pastaSauce);

        var subtotal = Math.round(((lipstick.getPrice() + pastaSauce.getPrice()) * 100.0) / 100.0);
        var taxableSubtotal = Math.round(((lipstick.getPrice() + pastaSauce.getPrice()) * 100.0) / 100.0);
        var taxTotal = Math.round(((taxableSubtotal * TAX_RATE) * 100.0) / 100.0);
        var grandTotal = Math.round(((taxTotal + subtotal) * 100.0) / 100.0);

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
                .price(3.99f)
                .build();
        Item cucumber = Item.builder()
                .isTaxable(false)
                .itemName("Cucumber")
                .ownBrand(false)
                .sku(654321)
                .price(0.99f)
                .build();
        Item pastaSauce = Item.builder()
                .isTaxable(true)
                .itemName("Central Market Pasta Sauce")
                .ownBrand(true)
                .sku(223344)
                .price(3.99f)
                .build();
        List<Item> items = List.of(lipstick, cucumber, pastaSauce);

        var subtotal = Math.round(((lipstick.getPrice() + cucumber.getPrice() + pastaSauce.getPrice()) * 100.0) / 100.0);
        var taxableSubtotal = Math.round(((lipstick.getPrice() + pastaSauce.getPrice()) * 100.0) / 100.0);
        var taxTotal = Math.round(((taxableSubtotal * TAX_RATE) * 100.0) / 100.0);
        var grandTotal = Math.round(((taxTotal + subtotal) * 100.0) / 100.0);
        
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
