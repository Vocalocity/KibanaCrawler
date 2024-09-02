package com.vonage.kibana_crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vonage.kibana_crawler.builder.AppCustomizedKibanaRequestBuilder;
import com.vonage.kibana_crawler.pojo.AppCustomizedKibanaRequest;
import com.vonage.kibana_crawler.pojo.KibanaRequestHeader;
import com.vonage.kibana_crawler.pojo.kibana_response.KibanaResponse;
import com.vonage.kibana_crawler.service.FileService;
import com.vonage.kibana_crawler.service.kibana_service.KibanaAPIService;
import com.vonage.kibana_crawler.utilities.DateBatchProducer;
import com.vonage.kibana_crawler.utilities.Helpers;
import com.vonage.kibana_crawler.utilities.KibanaResponseHelper;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class KibanaCrawler implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(KibanaCrawler.class);

	private final KibanaAPIService kibanaAPIService;

	private final FileService fileService;

	public static void main(String[] args) {
		SpringApplication.run(KibanaCrawler.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
/*
		String str = "/appserver/rest/click2callme/gRO87Hf1ey7EiccozGKRweCAxNIH+gCdvLR0tEwdG3ZLI5jS1N89Dfe7Yl7D39eXk31MNHL/QEaLLagt2BLUyhOa7APtrZqaY9exXH1N7Ksqw/+VMVqO";
		String[] split = str.split("/");
		removeDynamics(split);
		log.info(String.join("/", split));

 */


		kibanaAPIService.setHeaders(
				new KibanaRequestHeader(
						"security_authentication_saml1=Fe26.2**c6150e950074ce606cb7d9710bc51502fa047e2a48a4e864790715f3ad15b368*fd3gqw_6r13jFPMFKwZGcA*iUgOHq7f5txKAh43d835S_70pymijEQnFMe10ml-exOjqdMhzAVqLezPIzmSZgdRNrMgmWuO8R_jHR-W_az-IZ9ML4pAejuef2akqT_FeYiByhHXuJid7PYqgPCZWQLqARyF6TTQK33ofExRb6NwgYUw_KvOzmBiZHb_lMDKC4CBttVtw7IFftnquwPAEjUyOpA_iyB7L-1lrOPD5ho9DaZZtDLrvKh3cpMtKSb7oFeu9p2k7Ye-yUvWWMoKmCyTQJfqCCIeM7l_iCwuGp86zQQEzWhkmrhPMRgDGHIBytdsBxpgQAebMozYrMTEiiTgMT1SbR0hr6Jle5pTFlgyAvJY5RlmENOzKVRBKwWQLHD7MlAsx7If117QHMAhLn-qEGT1N9kY71oBu_O-HfQxyvkKQdVu2XmoEMRImslT3NT3eLmQkhWNtXc8z3KOSnx1lDsV1GFnYP0L-vxG83yUXMd0UuMEX_rID30HvopQ3ag47qB7tiNiKanYzoUXJfwN74lIA4uVsyQDZwZ67MF-tDHKI8NSVlonp-Sgfo6q3xJ_qzU5O1LjqFaVO6eDV5Xb**d2e98f465bbbfc5e7e7ba06225fd240fd727b4d2d905341a992c8972dfbc3415*tXeCeD8dxGCqSb0R9d2sY4rdAIu6pIcxuwEfBWAbRx8; security_authentication=Fe26.2**620105e63f4f53884584a31e46b6826727154ad65481e995d2a7c115f3afd8c6*3clMb6wdlTr-igV3qpD_SA*Hk8d4tyuqdyiWP5kepD3gDQ_-dLj64TKdle9VOhCcmxORk_wVmxkJo-NLt608fjbySsiQWYc5OxE_UHFciratkmKEhKVHXBPq3XcdP-Tra5I7-Qx26gicWzK_zxc3a83BofAKRu7UMDbtozdPwvKjEMOErMZEb9CUVFlGnZoin-Nce8aZ_1TcDefVXWo27ob**f385870abad9c1191531c1791f74030a69299c34b52b01feddc7894339bb6642*Ux3hOxLcq1py8pa754WsodHGh5QKBqh0lB0Gu9TLO4I; s_ecid=MCMID%7C88637971609207160601510393130151969564; cjConsent=MHxOfDB8Tnww; _fbp=fb.1.1713420501237.1770913534; _hjSessionUser_2882478=eyJpZCI6IjIyZmY4YzJiLWQ2MjEtNTI5ZC1hZGMwLTBmOTBjMjMzNTQ1ZSIsImNyZWF0ZWQiOjE3MTM0MjA1MDE0MzMsImV4aXN0aW5nIjp0cnVlfQ==; OptanonAlertBoxClosed=2024-04-18T06:08:23.388Z; FPID=3856e46c-e68a-4383-816c-773be80ae5d7; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+03+2024+11%3A58%3A57+GMT%2B0530+(India+Standard+Time)&version=202404.1.0&browserGpcFlag=0&isIABGlobal=false&hosts=&landingPath=NotLandingPage&groups=C0004%3A1%2CC0002%3A1%2CC0003%3A1%2CC0001%3A1%2CC0010%3A1&geolocation=IN%3BKA&AwaitingReconsent=false; dtm_token_sc=AQADJKC_6z5FGwFKUK69AQA_0wABAQCO7UZa5AEBAI7tRlrk; dtm_token=AQADJKC_6z5FGwFKUK69AQA_0wABAQCO7UZa5AEBAI7tRlrk; cjLiveRampLastCall=2024-06-14T09:00:52.045Z; cf_clearance=661yj8gOGlw8I3OXIZh0bXEg9jtGXWzxD8qNYFkM_o4-1718605516-1.0.1.1-JTAQrdbUJbh1bq1yejal.N4LcThdws7s6eZITkac3B8Lmi2.YHWpx95rhx8GobRPdOrexjg_phR6AjLe3vi9eA; mbox=PC#cad6617529ea41d6bddbe428da8d6496.34_0#1781850324|session#44bfe9a95ebf43b698908e040c584ed4#1718607384; _uetvid=0ebdbaf0fd4a11eea73ffd4ace3b6e90; AMCV_null%40AdobeOrg=-1124106680%7CMCMID%7C88637971609207160601510393130151969564%7CMCIDTS%7C19892%7CMCCIDH%7C-1720305254%7CMCOPTOUT-1718612725s%7CNONE%7CvVersion%7C5.2.0; _rdt_uuid=1713420499245.38d26b82-8606-4ffd-80af-c37ad1103531; s_nr=1718844978937-Repeat; _ga_8P345284G5=GS1.1.1718844980.11.0.1718844980.60.0.0; datadome=Eo5cSPsETNGemxarFs_amDnerA2DfRqccAeod5_uBiuYKGwLqVywAUh45ROLxy~Ti_PwsTV3_qaYrKlaK9dfgR3GszS3Bak3z6Th80qR8sk0XcorglOukQCjw2sElX3g; _ga_2FRRGV82Y0=GS1.1.1722446138.41.1.1722446166.0.0.0; _ga_EXYSW53ZZK=GS1.1.1724744326.37.1.1724744542.19.0.0; _ga=GA1.2.272996441.1713416589; amplitude_id_80d4ab359b88b06abffba0ea237ef107vonage.com=eyJkZXZpY2VJZCI6IjJkZTFiZDY0LTgxZDctNDA1OC1hZmUyLTE0ZmI2ZWFlMGQ2ZFIiLCJ1c2VySWQiOiJfbXBhbmRleSIsIm9wdE91dCI6ZmFsc2UsInNlc3Npb25JZCI6MTcyNDc2NjcxOTE0NiwibGFzdEV2ZW50VGltZSI6MTcyNDc2NjcyMDM4NCwiZXZlbnRJZCI6ODM2LCJpZGVudGlmeUlkIjozMDIsInNlcXVlbmNlTnVtYmVyIjoxMTM4fQ==; s_nr365=1724766720437-Repeat; AMCVS_A8833BC75245AF9E0A490D4D%40AdobeOrg=1; AMCV_A8833BC75245AF9E0A490D4D%40AdobeOrg=1176715910%7CMCIDTS%7C19964%7CMCMID%7C88637971609207160601510393130151969564%7CMCAAMLH-1725512454%7C7%7CMCAAMB-1725512454%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1724914854s%7CNONE%7CMCAID%7CNONE%7CvVersion%7C5.4.0; amp_f477e8=MUWynDgcdgdyHFL3ia5dvl...1i6e83p78.1i6e9gk1m.nf.e9.15o",
						"2.13.0",
						"osd-fetch"
				).formHeaders()
		);
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusDays(14);
		Map<String, Boolean> uniqueURIs = new ConcurrentHashMap<>();
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		DateBatchProducer dateBatchProducer = DateBatchProducer.defaultDateBatchProducer(start, end);
		while(dateBatchProducer.hasNext()){
			final Pair<LocalDateTime, LocalDateTime> batch = dateBatchProducer.next();
			threadPool.submit(() -> {
				Queue<KibanaResponse> container = new LinkedList<>();
				AppCustomizedKibanaRequest request = new AppCustomizedKibanaRequestBuilder()
						.addMustNot(new MutablePair<>("account", "QA"))
						.index("vbc_platform_services_team*")
						.size(10000)
						.addMultiMatch( "uri")
						.addMultiMatch("\"auth_type\":\"skip\"")
						.addRange("timestamp", batch.getLeft().toString(), batch.getRight().toString())
						.build();
				kibanaAPIService.sendRequest(request, container);
				while(!container.isEmpty()){
					KibanaResponse response = container.poll();
					KibanaResponseHelper.getMessage(response).forEach( msg -> {
								String[] split = msg.split("\\[com.vocalocity.hdap.logging.RequestLogFilter]");
								if(split.length == 2){
									String requestInfoJson = split[1];
                                    try {
                                        Map<String, Object> requestInfo = CrawlerConstants.MAPPER.readValue(requestInfoJson,
                                                new TypeReference<Map<String, Object>>() {});
										String uri = (String) requestInfo.get("uri");
										String[] uriComponents = uri.split("/");
										removeDynamics(uriComponents);
										log.info("URI {} was changed to {}", uri, String.join("/", uriComponents));
										uniqueURIs.put(String.join("/", uriComponents), true);
                                    } catch (JsonProcessingException e) {
										log.error("Error while parsing request info. \n {} \n", requestInfoJson, e);
                                    }
                                }
							}
					);
				}
			});
		}
		Helpers.shutdown(threadPool, 1, TimeUnit.HOURS);
		fileService.writeResult(new File("UniqueURI.txt"), true, String.join("\n", uniqueURIs.keySet()));
	}

	private static void removeDynamics(String[] uriComponents){
		for(int i = 0; i < uriComponents.length; i++){
			String uriComponent = uriComponents[i];
			if(uriComponent.isEmpty()) continue;
			int chars = uriComponent.length();
			int hyphenCount = 0;
			int upperCaseCount = 0;
			int charCount = 0;
			int numCount = 0;
			int percentageCount = 0;
			for(int index = 0; index < chars; index++){
				int ascii = uriComponent.charAt(index);
				if(uriComponent.charAt(index) == '-'){
					hyphenCount++;
				} else if (Character.isUpperCase(uriComponent.charAt(index))) {
					upperCaseCount++;
				} else if(48 <= ascii && ascii <= 57){
					numCount++;
				} else if (uriComponent.charAt(index) == '%') {
					percentageCount++;
				} else charCount++;
			}
			if(hyphenCount == 4){
				uriComponents[i] = "<UUID>";
			} else if (percentageCount > 0 || charCount > 90 || uriComponent.endsWith("==") || uriComponent.contains("+")) {
				uriComponents[i] = "<TOKEN>";
			}
			else if(numCount == chars) {
				uriComponents[i] = "<ID>";
			} else if (upperCaseCount > 1 && upperCaseCount < chars && numCount < chars && (chars - numCount - upperCaseCount) > 0) {
				uriComponents[i] = "<TOKEN>";
			}
		}
	}
}