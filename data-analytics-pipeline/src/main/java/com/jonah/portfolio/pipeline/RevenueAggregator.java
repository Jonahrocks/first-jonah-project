package com.jonah.portfolio.pipeline;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RevenueAggregator {

    public RevenueSummary summarize(List<SalesRecord> records) {
        BigDecimal totalRevenue = records.stream()
                .map(SalesRecord::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> revenueByRegion = aggregate(records, SalesRecord::region);
        Map<String, BigDecimal> revenueByProduct = aggregate(records, SalesRecord::product);

        return new RevenueSummary(scale(totalRevenue), revenueByRegion, revenueByProduct);
    }

    private Map<String, BigDecimal> aggregate(List<SalesRecord> records,
                                              java.util.function.Function<SalesRecord, String> classifier) {
        return records.stream().collect(Collectors.groupingBy(
                classifier,
                Collectors.mapping(SalesRecord::revenue,
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
        )).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> scale(entry.getValue())));
    }

    private BigDecimal scale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
