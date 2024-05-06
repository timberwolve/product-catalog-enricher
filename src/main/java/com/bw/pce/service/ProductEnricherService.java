package com.bw.pce.service;

import com.bw.pce.exceptions.LoadingProductsDictionaryException;
import com.bw.pce.model.Product;
import com.bw.pce.validator.ProductCsvRowValidator;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProductEnricherService {

   private static final Logger log = LoggerFactory.getLogger(ProductEnricherService.class);

   public List<Product> enrich(MultipartFile document) throws CsvException, IOException, LoadingProductsDictionaryException {
        var productList = new ArrayList<Product>();
        final var csvReader = new CSVReaderBuilder(new InputStreamReader(document.getInputStream()))
                 .withRowValidator(new ProductCsvRowValidator())
                 .build();
        final var map = createProductMap();
         productList.addAll(csvReader.readAll().parallelStream().map(s -> new Product(LocalDate.parse(s[0]), map.get(s[1]),
                    Currency.getInstance(s[2]), new BigDecimal(s[3])))
                 .collect(Collectors.toList()));
      return productList;
   }


   protected Map<String, String> createProductMap() throws LoadingProductsDictionaryException {
      var productMap = new HashMap<String, String>();
      try (Reader reader = new FileReader(ResourceUtils.getFile("classpath: static/product.csv"))){
         CSVReader csvReader = new CSVReaderBuilder(reader)
                 .build();
         productMap.putAll(csvReader.readAll().stream()
                 .collect(Collectors.toMap(s -> s[0], s -> s[1])));
      } catch (IOException | CsvException e) {
         log.error("Problem loading product dictionary {}" + e.getMessage());
         throw new LoadingProductsDictionaryException("Problem loading product dictionary", e);
      }
      return productMap;
   }
}
