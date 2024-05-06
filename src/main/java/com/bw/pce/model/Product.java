package com.bw.pce.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public record Product(@CsvBindByName(column = "date")
                      @CsvBindByPosition(position = 0)
                      LocalDate date,
                      @CsvBindByName(column = "product_name")
                      @CsvBindByPosition(position = 1)
                      String productName,
                      @CsvBindByName(column = "currency")
                      @CsvBindByPosition(position = 2)
                      Currency currency,
                      @CsvBindByName(column = "price")
                      @CsvBindByPosition(position = 3)
                      BigDecimal price) {
}
