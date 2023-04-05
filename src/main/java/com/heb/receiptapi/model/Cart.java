package com.heb.receiptapi.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * A cart full of grocery items
 */
@Data
@Value
@Builder
@Jacksonized
public class Cart {
    private List<Item> items;
}
