package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.model.Headline;
import com.mcgowan.newsnow.service.INewsNow;
import com.mcgowan.newsnow.service.IWebPageLoader;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class NewsNow implements INewsNow {

    private final IWebPageLoader webPageLoader;

    private final Twitter twitter;

    private ResponseList<Status> currentTweets;

    @Inject
    public NewsNow(IWebPageLoader webPageLoader, Twitter twitter) {
        this.webPageLoader = webPageLoader;
        this.twitter = twitter;
    }

    @Override
    public void process() throws IOException, TwitterException {
        currentTweets = twitter.getLatestTweets();

        Document doc = webPageLoader.getWebPage("http://www.newsnow.co.uk/h/World+News/Europe/Western/Republic+of+Ireland");
        List<Element> links = doc.getElementsByClass("hll").stream().limit(20).collect(Collectors.toList());
        log.info(links);
        List<Headline> headlinesToTweet = links.stream()
                .filter(this::isDuplicateTweet)
                .map(this::createHeadline)
                .collect(Collectors.toList());

        if (!headlinesToTweet.isEmpty())
            twitter.publish(headlinesToTweet);
        else
            log.info("No unique headlines to tweet. NFA");
    }

    public boolean isDuplicateTweet(Element e) {
        String title = e.text().trim();
        List<Status> matches = currentTweets.stream().filter(ct -> {
            String existingTitle = ct.getText().split("https:")[0].trim();
            return existingTitle.equals(title);
        }).collect(Collectors.toList());

        return !matches.isEmpty();
    }

    private Headline createHeadline(Element element) {
        try {
            return Headline.builder()
                    .headline(element.text())
                    .link(element.attr("href"))
                    .followOnLink(webPageLoader.getRedirectUrl(element.attr("href")))
                    .build();
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
