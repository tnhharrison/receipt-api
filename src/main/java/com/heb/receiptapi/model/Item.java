package com.heb.receiptapi.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

/**
 * Class describing a grocery item
 */
@Builder
@Data
@Value
public class Item {
    private String itemName;
    private long sku;
    private boolean isTaxable;
    private boolean ownBrand;
    private BigDecimal price;
}
