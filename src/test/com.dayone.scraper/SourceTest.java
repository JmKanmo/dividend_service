package com.dayone.scraper;

import com.dayone.exception.model.ScrapException;
import com.dayone.model.Company;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SourceTest {
    @Autowired
    private YahooFinanceScraper yahooFinanceScraper;


    @Test
    public void scrapTest() throws ScrapException {
        Assertions.assertNotNull(yahooFinanceScraper.scrap(new Company()));
    }
}
