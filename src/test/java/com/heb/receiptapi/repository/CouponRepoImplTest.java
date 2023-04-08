package com.heb.receiptapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class CouponRepoImplTest {
    static CouponRepoImpl couponRepoImpl = new CouponRepoImpl();

    @Test
    void testGetDiscountMissingFile() throws IOException {
        ReflectionTestUtils.setField(couponRepoImpl, "couponFilePath", "missing");
        BigDecimal actual = couponRepoImpl.getDiscount(0);
        BigDecimal expected = BigDecimal.valueOf(0);
        assertEquals(expected, actual);
    }
    
    @Test
    void testGetDiscountEmptyFile() throws IOException {
        ReflectionTestUtils.setField(couponRepoImpl, "couponFilePath", "src/test/resources/static/emptycoupons.json");
        BigDecimal actual = couponRepoImpl.getDiscount(123456);
        BigDecimal expected = BigDecimal.valueOf(0);
        assertEquals(expected, actual);
    }

    @Test
    void testGetDiscountNotFound() throws IOException {
        ReflectionTestUtils.setField(couponRepoImpl, "couponFilePath", "src/test/resources/static/coupons.json");
        BigDecimal actual = couponRepoImpl.getDiscount(0);
        BigDecimal expected = BigDecimal.valueOf(0);
        assertEquals(expected, actual);
    }

    @Test
    void testGetDiscount() throws IOException {
        ReflectionTestUtils.setField(couponRepoImpl, "couponFilePath", "src/test/resources/static/coupons.json");
        BigDecimal actual = couponRepoImpl.getDiscount(123456);
        BigDecimal expected = BigDecimal.valueOf(0.99);
        assertEquals(expected, actual);
    }
}
