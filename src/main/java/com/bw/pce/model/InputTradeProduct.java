package com.bw.pce.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public record InputTradeProduct(@CsvBindByName(column = "date")
                                @CsvBindByPosition(position = 0)
                                LocalDate date,
                                @CsvBindByName(column = "product_id")
                                @CsvBindByPosition(position = 1)
                                Long productId,
                                @CsvBindByName(column = "currency")
                                @CsvBindByPosition(position = 2)
                                Currency currency,
                                @CsvBindByName(column = "price")
                                @CsvBindByPosition(position = 3)
                                BigDecimal price) {
}
