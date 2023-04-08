package com.heb.receiptapi.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heb.receiptapi.model.Cart;
import com.heb.receiptapi.model.ReceiptResponse;
import com.heb.receiptapi.model.ReceiptWithDiscountsResponse;
import com.heb.receiptapi.service.CartService;

@WebMvcTest
public class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService service;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void testGetReceiptV1() throws JsonProcessingException, Exception {
        ReceiptResponse expected = ReceiptResponse.builder()
            .grandTotal(BigDecimal.valueOf(0))
            .subtotal(BigDecimal.valueOf(0))
            .taxTotal(BigDecimal.valueOf(0))
            .taxableSubtotal(BigDecimal.valueOf(0))
            .build();
            
        Mockito.when(service.checkout(anyList())).thenReturn(expected);
        Cart body = Cart.builder().items(List.of()).build();
        String jsonBody = mapper.writeValueAsString(body);
        this.mockMvc.perform(post("/v1/checkout")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void testGetReceiptV1BadMethod() throws JsonProcessingException, Exception {
        ReceiptResponse expected = ReceiptResponse.builder()
            .grandTotal(BigDecimal.valueOf(0))
            .subtotal(BigDecimal.valueOf(0))
            .taxTotal(BigDecimal.valueOf(0))
            .taxableSubtotal(BigDecimal.valueOf(0))
            .build();
            
        Mockito.when(service.checkout(anyList())).thenReturn(expected);
        this.mockMvc.perform(get("/v1/checkout"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetReceiptV2() throws JsonProcessingException, Exception {
        ReceiptWithDiscountsResponse expected = ReceiptWithDiscountsResponse.builder()
            .grandTotal(BigDecimal.valueOf(0))
            .discountTotal(BigDecimal.valueOf(0))
            .subtotalAfterDiscounts(BigDecimal.valueOf(0))
            .subtotalBeforeDiscounts(BigDecimal.valueOf(0))
            .taxableSubtotalAfterDiscounts(BigDecimal.valueOf(0))
            .taxTotal(BigDecimal.valueOf(0))
            .build();
            
        Mockito.when(service.checkoutWithCoupons(anyList())).thenReturn(expected);
        Cart body = Cart.builder().items(List.of()).build();
        String jsonBody = mapper.writeValueAsString(body);
        this.mockMvc.perform(post("/v2/checkout")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void testGetReceiptV2BadMethod() throws Exception {
        ReceiptWithDiscountsResponse expected = ReceiptWithDiscountsResponse.builder()
        .grandTotal(BigDecimal.valueOf(0))
        .discountTotal(BigDecimal.valueOf(0))
        .subtotalAfterDiscounts(BigDecimal.valueOf(0))
        .subtotalBeforeDiscounts(BigDecimal.valueOf(0))
        .taxableSubtotalAfterDiscounts(BigDecimal.valueOf(0))
        .taxTotal(BigDecimal.valueOf(0))
        .build();
        
    Mockito.when(service.checkoutWithCoupons(anyList())).thenReturn(expected);

    this.mockMvc.perform(get("/v2/checkout"))
        .andDo(print())
        .andExpect(status().is4xxClientError());
    }
}
