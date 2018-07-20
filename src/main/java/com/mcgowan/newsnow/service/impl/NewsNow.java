package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.model.Headline;
import com.mcgowan.newsnow.service.INewsNow;
import com.mcgowan.newsnow.service.IWebPageLoader;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class NewsNow implements INewsNow {

    private final IWebPageLoader webPageLoader;

    @Inject
    public NewsNow(IWebPageLoader webPageLoader) {
        this.webPageLoader = webPageLoader;
    }

    @Override
    public void process() throws IOException {

        Document doc = webPageLoader.getWebPage("http://www.newsnow.co.uk/h/World+News/Europe/Western/Republic+of+Ireland");
        Elements links = doc.getElementsByClass("hll");
//        log.info(links);
        List<Headline> headlines = links.stream().map(this::createHeadline).collect(Collectors.toList());
        log.info("====================== HEADLINES ==========================");
        log.info(headlines);

    }

    private Headline createHeadline(Element element) {
        return Headline.builder()
                .headline(element.text())
                .link(element.attr("href"))
                .build();
    }
}
