package com.mcgowan.newsnow.service;

import com.mcgowan.newsnow.model.Headline;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.List;

public interface ITwitter {
    void publish(Headline headline) throws TwitterException;

    void publish(List<Headline> headlines) throws TwitterException;

    void getTimeline() throws TwitterException;

    ResponseList<Status> getLatestTweets() throws TwitterException;
}
