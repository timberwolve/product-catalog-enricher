package com.bw.pce.controller;

import com.bw.pce.exceptions.LoadingProductsDictionaryException;
import com.bw.pce.exceptions.ProductEnrichException;
import com.bw.pce.model.InputTradeProductList;
import com.bw.pce.model.Product;
import com.bw.pce.service.ProductEnricherService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("api/v1")
public class ProductEnricherController {

    @Autowired
    private ProductEnricherService productEnricherService;

    /**
     * Enriches the provided document with product information and writes the enriched data as a CSV file to the response.
     *
     * @throws ProductEnrichException if there is a problem with product enrichment.
     */
    @PostMapping(path = "/enrich",consumes = {"text/csv"})
    @ResponseBody
    public ResponseEntity<Mono<InputStreamResource>> enrich(@RequestBody InputTradeProductList inputTradeProductList) throws ProductEnrichException, LoadingProductsDictionaryException {
        String fileName = String.format("%s.csv", RandomStringUtils.randomAlphabetic(10));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=" + fileName)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(productEnricherService.enrich(inputTradeProductList)
                        .flatMap(x -> {
                            InputStreamResource resource = new InputStreamResource(x);
                            return Mono.just(resource);
                        }));
    }

}
