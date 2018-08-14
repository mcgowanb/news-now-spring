package com.mcgowan.newsnow.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HeadlineTest {

    private final String HASHTAG = "#ireland";
    @Test
    public void testHeadlineLengthLessThanMinimumReq() {
        Headline h = Headline.builder()
                .followOnLink("www.example.com")
                .hashTag(HASHTAG)
                .headline("This is a headline")
                .build();

        String result = h.toString();
        assertTrue(result.length() <= 260);
    }

    @Test
    public void testHeadlineLengthGreaterThanMinimumReq() {
        Headline h = Headline.builder()
                .followOnLink("www.example.com")
                .hashTag(HASHTAG)
                .headline("This is a headline that is far greater than anything you could need for a twitter status and I will add the url as a longer one just in case, This is a headline that is far greater than anything you could need for a twitter status and I will add the url as a longer one just in case")
                .build();

        String result = h.toString();
        assertTrue(result.length() <= 260);
    }


}