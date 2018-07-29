package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.model.Headline;
import com.mcgowan.newsnow.service.ITwitter;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.util.List;

@Service
@Log4j2
@AllArgsConstructor
public class Twitter implements ITwitter {

    private final twitter4j.Twitter twitter;

    public Twitter() {
        twitter = new TwitterFactory().getInstance();
        log.info("creating twitter factory instance {}", twitter);
    }

    @Override
    public void publish(Headline headline) throws TwitterException {
        Status result = twitter.updateStatus(headline.toString());
        log.info("{} successfully published", result.getText());
    }

    @Override
    public void publish(List<Headline> headlines) {
        log.info("Publishing {} tweet(s)", headlines.size());
        for (Headline h : headlines) {
            try {
                publish(h);
            } catch (TwitterException ex) {
                log.error("Tweet {} is a duplicate", h.getHeadline());
            }
        }
    }

    @Override
    public void getTimeline() throws TwitterException {
        ResponseList<Status> timeline = twitter.getHomeTimeline();
        log.info(timeline);
    }

    @Override
    public ResponseList<Status> getLatestTweets() throws TwitterException {
        Paging p = new Paging();
        p.setCount(150);
        return twitter.getUserTimeline(p);
    }

}
