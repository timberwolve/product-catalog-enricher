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
        MultipartFile multipartFile = new MockMultipartFile("file",
                "Hello, World!".getBytes());

        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(multipartFile.getInputStream()).thenReturn(inputStream);

        Map<String, String> productMap = Map.of("P1", "Product1");

        ProductEnricherService productEnricherService = Mockito.spy(new ProductEnricherService());
        Mockito.doReturn(productMap).when(productEnricherService).createProductMap();

        List<Product> productList = productEnricherService.enrich(multipartFile);

        Assertions.assertNotNull(productList, "Product List should not be null");

        Assertions.assertFalse(productList.isEmpty(), "Product list should contain data");
        Assertions.assertEquals(productList.get(0).date(), LocalDate.parse("2023-03-15"), "Product date should be 15th March 2023");
        Assertions.assertEquals(productList.get(0).productName(), "P1", "Product Name should be P1");
        Assertions.assertEquals(productList.get(0).currency(), Currency.getInstance("USD"), "Currency should be USD");
        Assertions.assertEquals(productList.get(0).price(), new BigDecimal(80.45), "Price should be 80.45");
    }

    @Test
    @DisplayName("Test enrich Method, Failure Case")
    public void testEnrichFailure() throws CsvException, IOException, LoadingProductsDictionaryException {
        ProductEnricherService productEnricherService = new ProductEnricherService();

        MultipartFile multipartFile = new MockMultipartFile("file",new byte[0]);

        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(multipartFile.getInputStream()).thenReturn(inputStream);

        Assertions.assertThrows(CsvException.class,()-> productEnricherService.enrich(multipartFile),"enrich method should throw exception");
    }
}