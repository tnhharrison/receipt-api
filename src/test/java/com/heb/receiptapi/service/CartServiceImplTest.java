package com.heb.receiptapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.heb.receiptapi.model.Item;
import com.heb.receiptapi.model.ReceiptResponse;
import com.heb.receiptapi.model.ReceiptWithDiscountsResponse;
import com.heb.receiptapi.repository.CouponRepoImpl;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    CouponRepoImpl repo;

    @InjectMocks
    CartServiceImpl cartServiceImpl = new CartServiceImpl();

    @Test
    void testV1EmptyCart() {
        ReceiptResponse expected = ReceiptResponse.builder()
                .grandTotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .subtotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .taxTotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .taxableSubtotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .build();
        ReceiptResponse actual = cartServiceImpl.checkout(List.of());
        assertEquals(expected, actual);
    }

    @Test
    void testV1TaxRateCalc() {
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
        assertEquals(expected, actual);
    }

    @Test
    void testV1IsTaxableCalc() {
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
        assertEquals(expected, actual);
    }

    /* TEST CHECKOUT WITH DISCOUNT FUNCTION */
    /* Make sure we didn't break any of the basic math from the v1 functionality */

    @Test
    void testV2EmptyCart() {
        ReceiptWithDiscountsResponse expected = ReceiptWithDiscountsResponse.builder()
                .grandTotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .subtotalBeforeDiscounts(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .subtotalAfterDiscounts(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .taxTotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .taxableSubtotalAfterDiscounts(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .discountTotal(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP))
                .build();
                ReceiptWithDiscountsResponse actual = cartServiceImpl.checkoutWithCoupons(List.of());
        assertEquals(expected, actual);
    }

    @Test
    void testV2TaxRateCalc() {
        Mockito.when(repo.getDiscount(anyLong())).thenReturn(BigDecimal.valueOf(0));
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

        var taxableSubtotalAfterDiscounts = BigDecimal.valueOf(7.98).setScale(2, RoundingMode.HALF_UP);
        var subtotalBeforeDiscounts = BigDecimal.valueOf(7.98).setScale(2, RoundingMode.HALF_UP);
        // same as before discounts because should be 0 discounts
        var subtotalAfterDiscounts = BigDecimal.valueOf(7.98).setScale(2, RoundingMode.HALF_UP); 
        var discountTotal = BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP);
        var taxTotal = BigDecimal.valueOf(0.66).setScale(2, RoundingMode.HALF_UP);
        var grandTotal = BigDecimal.valueOf(8.64).setScale(2, RoundingMode.HALF_UP);

        ReceiptWithDiscountsResponse expected = ReceiptWithDiscountsResponse.builder()
                .grandTotal(grandTotal)
                .subtotalBeforeDiscounts(subtotalBeforeDiscounts)
                .subtotalAfterDiscounts(subtotalAfterDiscounts)
                .taxTotal(taxTotal)
                .taxableSubtotalAfterDiscounts(taxableSubtotalAfterDiscounts)
                .discountTotal(discountTotal)
                .build();
        ReceiptWithDiscountsResponse actual = cartServiceImpl.checkoutWithCoupons(items);
        assertEquals(expected, actual);
    }

    @Test
    void testV2IsTaxableCalc() {
        Mockito.when(repo.getDiscount(anyLong())).thenReturn(BigDecimal.valueOf(0));
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
        
        var taxableSubtotalAfterDiscounts = BigDecimal.valueOf(7.98).setScale(2, RoundingMode.HALF_UP);
        var subtotalBeforeDiscounts = BigDecimal.valueOf(8.97).setScale(2, RoundingMode.HALF_UP);
        // same as before discounts because should be 0 discounts
        var subtotalAfterDiscounts = BigDecimal.valueOf(8.97).setScale(2, RoundingMode.HALF_UP); 
        var discountTotal = BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP);
        var taxTotal = BigDecimal.valueOf(0.66).setScale(2, RoundingMode.HALF_UP);
        var grandTotal = BigDecimal.valueOf(9.63).setScale(2, RoundingMode.HALF_UP);

        ReceiptWithDiscountsResponse expected = ReceiptWithDiscountsResponse.builder()
                .grandTotal(grandTotal)
                .subtotalBeforeDiscounts(subtotalBeforeDiscounts)
                .subtotalAfterDiscounts(subtotalAfterDiscounts)
                .taxTotal(taxTotal)
                .taxableSubtotalAfterDiscounts(taxableSubtotalAfterDiscounts)
                .discountTotal(discountTotal)
                .build();
        ReceiptWithDiscountsResponse actual = cartServiceImpl.checkoutWithCoupons(items);
        assertEquals(expected, actual);
    }

    /* Test discount math */

    @Test
    void testV2DiscountFound() {
        Mockito.when(repo.getDiscount(anyLong())).thenReturn(BigDecimal.valueOf(0));
        Mockito.when(repo.getDiscount(123456)).thenReturn(BigDecimal.valueOf(.99));
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
        
        var taxableSubtotalAfterDiscounts = BigDecimal.valueOf(6.99).setScale(2, RoundingMode.HALF_UP);
        var subtotalBeforeDiscounts = BigDecimal.valueOf(8.97).setScale(2, RoundingMode.HALF_UP);
        var subtotalAfterDiscounts = BigDecimal.valueOf(7.98).setScale(2, RoundingMode.HALF_UP); 
        var discountTotal = BigDecimal.valueOf(0.99).setScale(2, RoundingMode.HALF_UP);
        var taxTotal = BigDecimal.valueOf(0.58).setScale(2, RoundingMode.HALF_UP);
        var grandTotal = BigDecimal.valueOf(9.55).setScale(2, RoundingMode.HALF_UP);

        ReceiptWithDiscountsResponse expected = ReceiptWithDiscountsResponse.builder()
                .grandTotal(grandTotal)
                .subtotalBeforeDiscounts(subtotalBeforeDiscounts)
                .subtotalAfterDiscounts(subtotalAfterDiscounts)
                .taxTotal(taxTotal)
                .taxableSubtotalAfterDiscounts(taxableSubtotalAfterDiscounts)
                .discountTotal(discountTotal)
                .build();
        ReceiptWithDiscountsResponse actual = cartServiceImpl.checkoutWithCoupons(items);
        assertEquals(expected, actual);
    }

    @Test
    void testV2FinalPriceNotNeg() {
        Mockito.when(repo.getDiscount(anyLong())).thenReturn(BigDecimal.valueOf(0));
        Mockito.when(repo.getDiscount(123456)).thenReturn(BigDecimal.valueOf(4.99));
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
        
        var taxableSubtotalAfterDiscounts = BigDecimal.valueOf(3.99).setScale(2, RoundingMode.HALF_UP);
        var subtotalBeforeDiscounts = BigDecimal.valueOf(8.97).setScale(2, RoundingMode.HALF_UP);
        var subtotalAfterDiscounts = BigDecimal.valueOf(4.98).setScale(2, RoundingMode.HALF_UP); 
        var discountTotal = BigDecimal.valueOf(3.99).setScale(2, RoundingMode.HALF_UP);
        var taxTotal = BigDecimal.valueOf(0.33).setScale(2, RoundingMode.HALF_UP);
        var grandTotal = BigDecimal.valueOf(9.30).setScale(2, RoundingMode.HALF_UP);

        ReceiptWithDiscountsResponse expected = ReceiptWithDiscountsResponse.builder()
                .grandTotal(grandTotal)
                .subtotalBeforeDiscounts(subtotalBeforeDiscounts)
                .subtotalAfterDiscounts(subtotalAfterDiscounts)
                .taxTotal(taxTotal)
                .taxableSubtotalAfterDiscounts(taxableSubtotalAfterDiscounts)
                .discountTotal(discountTotal)
                .build();
        ReceiptWithDiscountsResponse actual = cartServiceImpl.checkoutWithCoupons(items);
        assertEquals(expected, actual);
    }
}
