package com.cinematch.cinematch.runner;

import com.cinematch.cinematch.service.ColourScraperService;
import com.cinematch.cinematch.repository.DuluxColourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScraperRunner implements CommandLineRunner {

    private final ColourScraperService scraperService;
    private final DuluxColourRepository repository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running scraper...");
        scraperService.scrapeColors();
        System.out.println("Scrape complete - total colours scraped: " + repository.count());
    }
}
