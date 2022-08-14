package com.dayone.scraper;

import com.dayone.exception.model.ScrapException;
import com.dayone.model.Company;
import com.dayone.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker) throws ScrapException;

    ScrapedResult scrap(Company company) throws ScrapException;
}
