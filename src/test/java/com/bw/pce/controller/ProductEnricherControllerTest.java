package com.bw.pce.controller;

import com.bw.pce.service.ProductEnricherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductEnricherController.class)
@Import(ProductEnricherService.class)
public class ProductEnricherControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testEnrich() throws Exception {
        var bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("trade.csv", new String(new ClassPathResource("static/trade.csv").getContentAsByteArray()), MediaType.asMediaType(MimeType.valueOf("text/csv")));
        webClient.mutate().responseTimeout(Duration.ofMillis(30000)).build().post().uri("/api/v1/enrich").body(BodyInserters.fromMultipartData(bodyBuilder.build())).exchange().expectStatus().isOk();

    }
    
    @Test
    public void testEnrich_emptyProductList() throws Exception {

    }
    
    @Test
    public void testEnrich_Exception() throws Exception {

    }
}