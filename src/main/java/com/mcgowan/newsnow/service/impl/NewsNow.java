package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.model.Headline;
import com.mcgowan.newsnow.service.INewsNow;
import com.mcgowan.newsnow.service.IWebPageLoader;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class NewsNow implements INewsNow {

    private final IWebPageLoader webPageLoader;

    private final Twitter twitter;

    private ResponseList<Status> currentTweets;

    @Value("${headline.selector}")
    private String selector;

    @Value("${application.url}")
    private String uri;

    @Value("${tweet.hashtag}")
    private String hashTag;

    @Inject
    public NewsNow(IWebPageLoader webPageLoader, Twitter twitter) {
        this.webPageLoader = webPageLoader;
        this.twitter = twitter;
    }

    @Override
    public void process() throws IOException, TwitterException {
        currentTweets = twitter.getLatestTweets();
        Document doc = webPageLoader.getWebPage(uri);
        ArrayList<Headline> headlinesToTweet = doc.select(selector)
                .stream()
                .map(this::createHeadline)
                .filter(this::isDuplicateTweet)
                .collect(Collectors.toCollection(ArrayList::new));

        if (!headlinesToTweet.isEmpty()) {
            headlinesToTweet.forEach(this::addFollowOnLink);
            twitter.publish(headlinesToTweet);
        } else
            log.info("No unique headlines to tweet. NFA");
    }

    private boolean isDuplicateTweet(Headline e) {
        Integer cLength = e.getHeadline().length() >= 60 ? 60 : e.getHeadline().length();
        String title = e.getHeadline().substring(0, cLength).trim();
        List<Status> matches = currentTweets.stream()
                .filter(ct -> ct.getText().contains(title))
                .collect(Collectors.toCollection(ArrayList::new));
        return matches.isEmpty();
    }


    private Headline createHeadline(Element element) {
        return Headline.builder()
                .headline(element.getElementsByClass("hll").first().text())
                .hashTag(hashTag)
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
