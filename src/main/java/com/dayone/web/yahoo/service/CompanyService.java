package com.dayone.web.yahoo.service;

import com.dayone.exception.model.AlreadyExistCompanyException;
import com.dayone.exception.model.NoCompanyException;
import com.dayone.exception.model.ScrapException;
import com.dayone.model.Company;
import com.dayone.model.ScrapedResult;
import com.dayone.model.constants.Utils;
import com.dayone.persist.CompanyRepository;
import com.dayone.persist.DividendRepository;
import com.dayone.persist.entity.CompanyEntity;
import com.dayone.scraper.YahooFinanceScraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CompanyService {
    private final Trie trie;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final YahooFinanceScraper yahooFinanceScraper;

    public Company save(String ticker) throws ScrapException {
        Company company = yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (companyRepository.existsByTicker(ticker)) {
            throw new AlreadyExistCompanyException();
        }
        companyRepository.save(new CompanyEntity(company));
        return company;
    }

    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return companyRepository.findCompanyEntityBy(pageable);
    }

    private ScrapedResult storeCompanyAndDividend(String ticker) throws Exception {
        // 1. ticker 를 기준으로 회사를 스크래핑
        Company company = companyRepository.findByTicker(ticker)
                .map(companyEntity -> new Company(companyEntity.getName(), companyEntity.getTicker())).orElseThrow(() -> new NoCompanyException());

        // 2. 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        try {
            ScrapedResult scrapedResult = yahooFinanceScraper.scrap(company);
            return scrapedResult;
        } catch (ScrapException scrapException) {
            throw scrapException;
        }
    }

    public List<String> getCompanyNamesByKeyword(String keyword) {
        List<String> resultList = (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream()
                .collect(Collectors.toList());

        if (resultList.size() > Utils.TRIE_PREFIX_RESULT_MAX) {
            return resultList.subList(0, Utils.TRIE_PREFIX_RESULT_MAX);
        }
        return resultList;
    }

    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    @Transactional
    public String deleteCompany(String ticker) {
        // 1. 배당금 정보 삭제
        if (companyRepository.existsByTicker(ticker)) {
            CompanyEntity companyEntity = companyRepository.findByTicker(ticker).get();
            companyRepository.deleteByTicker(ticker);
            dividendRepository.deleteAllByCompanyId(companyEntity.getId());
            return companyEntity.getName();
        } else {
            throw new NoCompanyException();
        }
    }
}
