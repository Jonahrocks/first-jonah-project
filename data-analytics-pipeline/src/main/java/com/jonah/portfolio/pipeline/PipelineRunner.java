package com.jonah.portfolio.pipeline;

import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PipelineRunner {

    private static final Logger log = LoggerFactory.getLogger(PipelineRunner.class);

    private PipelineRunner() {
    }

    public static void main(String[] args) {
        Path inputPath = args.length > 0 ? Path.of(args[0]) : defaultResource();
        log.info("Starting analytics pipeline for {}", inputPath.toAbsolutePath());

        CsvSalesLoader loader = new CsvSalesLoader();
        List<SalesRecord> records = loader.load(inputPath);
        RevenueAggregator aggregator = new RevenueAggregator();
        RevenueSummary summary = aggregator.summarize(records);

        log.info("Total revenue: {}", summary.totalRevenue());
        summary.revenueByRegion().forEach((region, revenue) ->
                log.info("Region {} revenue {}", region, revenue));
        summary.revenueByProduct().forEach((product, revenue) ->
                log.info("Product {} revenue {}", product, revenue));
    }

    private static Path defaultResource() {
        try {
            return Path.of(PipelineRunner.class.getClassLoader()
                    .getResource("sales-data.csv").toURI());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to resolve default sales dataset", e);
        }
    }
}
