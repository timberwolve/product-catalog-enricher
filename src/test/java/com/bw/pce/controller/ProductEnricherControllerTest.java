package com.bw.pce.controller;

import com.bw.pce.exceptions.ProductEnrichException;
import com.bw.pce.model.Product;
import com.bw.pce.service.ProductEnricherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductEnricherController.class)
public class ProductEnricherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductEnricherService productEnricherService;

    @Test
    public void testEnrich() throws Exception {
        Product product = new Product(LocalDate.now(), "Test Product", Currency.getInstance("PLN"), new BigDecimal("123.45"));

        when(productEnricherService.enrich(any())).thenReturn(Collections.singletonList(product));

        MockMultipartFile file = new MockMultipartFile("test.csv", "test.csv", "text/csv", "test data".getBytes());
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/enrich")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        
        assertThat(response.getContentType()).isEqualTo("text/csv");
        assertThat(response.getHeader("Content-Disposition")).isEqualTo("attachment; filename=data.csv");
        assertThat(response.getContentAsString()).contains("Test Product");
    }
    
    @Test
    public void testEnrich_emptyProductList() throws Exception {
        when(productEnricherService.enrich(any())).thenReturn(Collections.emptyList());
        
        MockMultipartFile file = new MockMultipartFile("data.csv", "data.csv", "text/csv", "test data".getBytes());
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/enrich")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        
        assertThat(response.getContentType()).isEqualTo("text/csv");
        assertThat(response.getHeader("Content-Disposition")).isEqualTo("attachment; filename=data.csv");
        assertThat(response.getContentAsString()).isEmpty();
    }
    
    @Test
    public void testEnrich_Exception() throws Exception {
        when(productEnricherService.enrich(any())).thenThrow(new ProductEnrichException("Error"));
        
        MockMultipartFile file = new MockMultipartFile("data.csv", "data.csv", "text/csv", "test data".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/enrich")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError());
    }
}