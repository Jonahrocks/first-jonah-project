package com.jonah.portfolio.pipeline;

import java.math.BigDecimal;
import java.util.Map;

public record RevenueSummary(BigDecimal totalRevenue, Map<String, BigDecimal> revenueByRegion,
                             Map<String, BigDecimal> revenueByProduct) {
}
