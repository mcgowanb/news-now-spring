package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.model.Headline;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.List;

@Service
@Log4j2
@AllArgsConstructor
public class Twitter {

    private final twitter4j.Twitter twitter;

    public Twitter() {
        twitter = new TwitterFactory().getInstance();
        log.info("creating twitter factory instance {}", twitter);
    }

    public void publish(Headline headline) throws TwitterException {
        Status result = twitter.updateStatus(headline.toString());
        log.info("{} successfully published", result.getText());
    }

    public void publish(List<Headline> headlines) throws TwitterException {
        log.info("Publishing {} tweet(s)", headlines.size());
        for (Headline h : headlines) {
            try {
                publish(h);
            } catch (TwitterException ex) {
                log.error("Tweet {} is a duplicate", h.getHeadline());
                log.error(ex);
            }
        }
    }

    public void getTimeline() throws TwitterException {
        ResponseList<Status> timeline = twitter.getHomeTimeline();
        log.info(timeline);
    }

    public ResponseList<Status> getLatestTweets() throws TwitterException {
        return twitter.getUserTimeline();
    }

}
