package com.bw.pce.validator;

import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.RowValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ProductCsvRowValidator implements RowValidator {
    private static final Logger log = LoggerFactory.getLogger(ProductCsvRowValidator.class);
    @Override
    public boolean isValid(String[] row) {
        try {
            this.validate(row);
        } catch (CsvValidationException e){
            log.warn("Line not valid will be omitted {}", Arrays.toString(row));
            return false;
        }
        return true;
    }

    @Override
    public void validate(String[] row) throws CsvValidationException {
        try {
            LocalDate.parse(row[0],
                    DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            //throw new CsvValidationException("Problem with date parsing.");
        }
    }
}
