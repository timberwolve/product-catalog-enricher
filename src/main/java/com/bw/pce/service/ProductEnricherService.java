package com.bw.pce.service;

import com.bw.pce.exceptions.LoadingProductsDictionaryException;
import com.bw.pce.exceptions.ProductEnrichException;
import com.bw.pce.model.InputTradeProduct;
import com.bw.pce.model.InputTradeProductList;
import com.bw.pce.model.Product;
import com.bw.pce.utils.ByteArrayInOutStream;
import com.bw.pce.validator.ProductCsvRowValidator;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProductEnricherService {

   private static final Logger log = LoggerFactory.getLogger(ProductEnricherService.class);

   public Mono<ByteArrayInputStream> enrich(InputTradeProductList inputTradeProductList) {

      return Mono.fromCallable(() -> {
          try {
              ByteArrayInOutStream stream = new ByteArrayInOutStream();
              OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
              CSVWriter writer = new CSVWriter(streamWriter);

              final var mappingStrategy = new HeaderColumnNameMappingStrategyBuilder<Product>().build();

              StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder<Product>(writer)
                      .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                      .withMappingStrategy(mappingStrategy)
                      .withSeparator(',')
                      .build();

              beanToCsv.write(enrichProductList(inputTradeProductList));
              streamWriter.flush();
              return stream.getInputStream();
          }
          catch (CsvException | IOException e) {
              log.error("Problem enriching products {}" + e.getMessage());
              throw new ProductEnrichException("Problem enriching products", e);
          }

      }).subscribeOn(Schedulers.boundedElastic());
   }

   private List<Product> enrichProductList(InputTradeProductList inputTradeProductList) throws ProductEnrichException{
       try {
           var productList = new ArrayList<Product>();
           var map = createProductMap();
           productList.addAll(inputTradeProductList.getList().stream().parallel().map(tradeProduct -> new Product(tradeProduct.getDate(), map.get(tradeProduct.getProductId()),
                           tradeProduct.getCurrency(), tradeProduct.getPrice()))
                   .collect(Collectors.toList()));
           return productList;
       } catch (LoadingProductsDictionaryException e) {
           log.error("Problem enriching products {}" + e.getMessage());
           throw new ProductEnrichException("Problem enriching products", e);
       }

   }


   protected Map<String, String> createProductMap() throws LoadingProductsDictionaryException {
      var productMap = new HashMap<String, String>();
      try (Reader reader = new FileReader(ResourceUtils.getFile("classpath:static/product.csv"))){
         CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1)
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
