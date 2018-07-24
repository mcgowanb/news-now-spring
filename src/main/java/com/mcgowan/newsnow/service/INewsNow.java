package com.mcgowan.newsnow.service;

import twitter4j.TwitterException;

import java.io.IOException;

public interface INewsNow {
    void process() throws IOException, TwitterException;
}
