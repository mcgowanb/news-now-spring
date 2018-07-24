package com.mcgowan.newsnow.service.impl;

import com.mcgowan.newsnow.model.Headline;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
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
        for (Headline h : headlines) {
            publish(h);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}
