package com.bw.pce.service;

import com.bw.pce.exceptions.LoadingProductsDictionaryException;
import com.bw.pce.model.Product;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public class ProductEnricherServiceTest {

    @Test
    @DisplayName("Test enrich Method, Happy Path")
    public void testEnrich() throws CsvException, IOException, LoadingProductsDictionaryException {

    }

    @Test
    @DisplayName("Test enrich Method, Failure Case")
    public void testEnrichFailure() throws CsvException, IOException, LoadingProductsDictionaryException {
        ProductEnricherService productEnricherService = new ProductEnricherService();

        MultipartFile multipartFile = new MockMultipartFile("file",new byte[0]);

        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(multipartFile.getInputStream()).thenReturn(inputStream);

        //Assertions.assertThrows(CsvException.class,()-> productEnricherService.enrich(multipartFile),"enrich method should throw exception");
    }
}