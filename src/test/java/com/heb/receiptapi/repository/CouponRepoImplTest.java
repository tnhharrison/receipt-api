package com.heb.receiptapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class CouponRepoImplTest {
    static CouponRepoImpl couponRepoImpl = new CouponRepoImpl();

    @Test
    void testGetDiscountMissingFile() throws IOException {
        ReflectionTestUtils.setField(couponRepoImpl, "couponFilePath", "missing");
        float actual = couponRepoImpl.getDiscount(0);
        float expected = 0;
        assertEquals(expected, actual);
    }
    
    @Test
    void testGetDiscountEmptyFile() throws IOException {
        ReflectionTestUtils.setField(couponRepoImpl, "couponFilePath", "src/test/resources/static/emptycoupons.json");
        float actual = couponRepoImpl.getDiscount(123456);
        float expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    void testGetDiscountNotFound() throws IOException {
        ReflectionTestUtils.setField(couponRepoImpl, "couponFilePath", "src/test/resources/static/coupons.json");
        float actual = couponRepoImpl.getDiscount(0);
        float expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    void testGetDiscount() throws IOException {
        ReflectionTestUtils.setField(couponRepoImpl, "couponFilePath", "src/test/resources/static/coupons.json");
        float actual = couponRepoImpl.getDiscount(123456);
        float expected = 0.99f;
        assertEquals(expected, actual);
    }
}
