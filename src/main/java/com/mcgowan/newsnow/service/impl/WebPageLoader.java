package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.service.IWebPageLoader;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Log4j2
public class WebPageLoader implements IWebPageLoader {

    @Override
    public Document getWebPage(final String url) throws IOException {
        log.info("connecting to {}", url);
        return Jsoup.connect(url).get();
    }

    @Override
    public String getRedirectUrl(String link) throws IOException {
        return getWebPage(link)
                .select("#retrieval-msg > p a")
                .attr("href");
    }

}
