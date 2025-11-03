package com.jonah.portfolio.pipeline;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvSalesLoader {

    public List<SalesRecord> load(Path path) {
        try (Reader reader = Files.newBufferedReader(path);
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(reader)) {
            return parser.stream()
                    .map(this::toRecord)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load CSV %s".formatted(path), e);
        }
    }

    private SalesRecord toRecord(CSVRecord record) {
        return new SalesRecord(
                record.get("region"),
                record.get("product"),
                Integer.parseInt(record.get("units")),
                new BigDecimal(record.get("unit_price"))
        );
    }
}
