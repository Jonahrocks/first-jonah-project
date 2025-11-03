package com.jonah.portfolio.pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RevenueAggregatorTest {

    private List<SalesRecord> records;

    @BeforeEach
    void setUp() {
        CsvSalesLoader loader = new CsvSalesLoader();
        records = loader.load(Path.of("src/main/resources/sales-data.csv"));
    }

    @Test
    void shouldAggregateRevenueByRegionAndProduct() {
        RevenueAggregator aggregator = new RevenueAggregator();
        RevenueSummary summary = aggregator.summarize(records);

        assertThat(summary.totalRevenue()).isEqualByComparingTo(new BigDecimal("6867.36"));
        assertThat(summary.revenueByRegion())
                .containsEntry("EMEA", new BigDecimal("3284.48"))
                .containsEntry("AMER", new BigDecimal("2958.47"))
                .containsEntry("APAC", new BigDecimal("624.41"));
        assertThat(summary.revenueByProduct())
                .containsEntry("Analytics Pro", new BigDecimal("2599.87"))
                .containsEntry("Data Lake", new BigDecimal("3581.00"))
                .containsEntry("Insight Plus", new BigDecimal("686.49"));
    }
}
