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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class NewsNow implements INewsNow {

    private final IWebPageLoader webPageLoader;

    private final Twitter twitter;

    private ResponseList<Status> currentTweets;

    private String url;

    private static final String SELECTOR = "div.newsmain_wrap.central_ln_wrap.h-spacing--top .hl";

    @Inject
    public NewsNow(IWebPageLoader webPageLoader, Twitter twitter) {
        this.webPageLoader = webPageLoader;
        this.twitter = twitter;
    }

    @Override
    public void process() throws IOException, TwitterException {
        currentTweets = twitter.getLatestTweets();

        url = "http://www.newsnow.co.uk/h/World+News/Europe/Western/Republic+of+Ireland";
        Document doc = webPageLoader.getWebPage(url);
        List<Element> links = doc.select(SELECTOR);

        List<Headline> headlinesToTweet = links.stream()
                .map(this::createHeadline)
                .sorted(Comparator.comparing(Headline::getCreated).reversed())
                .filter(this::isDuplicateTweet)
                .limit(20)
                .collect(Collectors.toList());

        if (!headlinesToTweet.isEmpty()) {
            headlinesToTweet.forEach(this::addFollowOnLink);
            twitter.publish(headlinesToTweet);
        } else
            log.info("No unique headlines to tweet. NFA");
    }

    public boolean isDuplicateTweet(Headline e) {
        String title = e.getHeadline().trim();
        List<Status> matches = currentTweets.stream().filter(ct -> {
            String existingTitle = ct.getText().split("https:")[0].trim();
            return existingTitle.equals(title);
        }).collect(Collectors.toList());
        return matches.isEmpty();
    }


    private Headline createHeadline(Element element) {
        return Headline.builder()
                .headline(element.getElementsByClass("hll").first().text())
                .link(element.getElementsByClass("hll").first().attr("href"))
                .created(new Date(Long.parseLong(element.getElementsByClass("time")
                        .first().attr("data-time")) * 1000))
                .build();
    }

    private Headline addFollowOnLink(Headline h) {
        try {
            h.setFollowOnLink(webPageLoader.getRedirectUrl(h.getLink()));
        } catch (IOException e) {
            log.error(e);
        }
        return h;
    }
}
