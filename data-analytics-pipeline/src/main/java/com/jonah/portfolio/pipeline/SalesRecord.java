package com.jonah.portfolio.pipeline;

import java.math.BigDecimal;

public record SalesRecord(String region, String product, int units, BigDecimal unitPrice) {
    public BigDecimal revenue() {
        return unitPrice.multiply(BigDecimal.valueOf(units));
    }
}
