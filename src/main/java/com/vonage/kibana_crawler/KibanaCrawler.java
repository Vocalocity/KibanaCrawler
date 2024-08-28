package com.vonage.kibana_crawler;

import com.vonage.kibana_crawler.builder.AppCustomizedKibanaRequestBuilder;
import com.vonage.kibana_crawler.pojo.KibanaRequestHeader;
import com.vonage.kibana_crawler.service.kibana_service.KibanaAPIService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
@RequiredArgsConstructor
public class KibanaCrawler implements CommandLineRunner {

	private final KibanaAPIService kibanaAPIService;

	public static void main(String[] args) {
		SpringApplication.run(KibanaCrawler.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}





















