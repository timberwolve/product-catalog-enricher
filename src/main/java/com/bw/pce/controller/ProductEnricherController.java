package com.bw.pce.controller;

import com.bw.pce.exceptions.LoadingProductsDictionaryException;
import com.bw.pce.exceptions.ProductEnrichException;
import com.bw.pce.model.Product;
import com.bw.pce.service.ProductEnricherService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController("/api")
public class ProductEnricherController {

    @Autowired
    private ProductEnricherService productEnricherService;

    /**
     * Enriches the provided document with product information and writes the enriched data as a CSV file to the response.
     *
     * @param document              The document to be enriched.
     * @param httpServletResponse  The HTTP servlet response object.
     * @throws ProductEnrichException if there is a problem with product enrichment.
     */
    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(path = "/v1/enrich", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void enrich(@RequestPart MultipartFile document, final HttpServletResponse httpServletResponse) throws ProductEnrichException {
        try {
            List<Product> productList = productEnricherService.enrich(document);
            var headerKey = "Content-Disposition";
            var headerValue = "attachment; filename=data.csv";
            httpServletResponse.setContentType("text/csv");
            httpServletResponse.setHeader(headerKey, headerValue);
            final var mappingStrategy = new HeaderColumnNameMappingStrategyBuilder<Product>().build();
            final StatefulBeanToCsv<Product> statefulBeanToCsv = new StatefulBeanToCsvBuilder<Product>(httpServletResponse.getWriter())
                    .withMappingStrategy(mappingStrategy)
                    .withSeparator(',')
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            statefulBeanToCsv.write(productList);
        } catch (IOException | CsvException | LoadingProductsDictionaryException ex) {
            throw new ProductEnrichException("Problem with product enrichment! ", ex);
        }
    }

}
