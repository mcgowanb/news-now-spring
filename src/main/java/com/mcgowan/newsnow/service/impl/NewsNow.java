package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.service.INewsNow;
import com.mcgowan.newsnow.service.IWebPageLoader;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;

@Log4j2
@Service
public class NewsNow implements INewsNow {

    private final IWebPageLoader webPageLoader;

    @Inject
    public NewsNow(IWebPageLoader webPageLoader) {
        this.webPageLoader = webPageLoader;
    }

    @Override
    public void process() {
        log.info("processing");

        Document doc = null;
        try {
            doc = webPageLoader.getWebPage("http://www.newsnow.co.uk/h/World+News/Europe/Western/Republic+of+Ireland");
            log.info(doc);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
}
