package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.service.IWebPageLoader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebPageLoader implements IWebPageLoader {

    @Override
    public Document getWebPage(final String url) throws IOException {
        return Jsoup.connect(url).get();
    }

}
