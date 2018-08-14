package com.mcgowan.newsnow.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
public class Headline {

    private String headline;

    private String link;

    private String followOnLink;

    private Date created;

    private final Integer TWEET_LENGTH = 260;

    private String hashTag;

    @Override
    public String toString() {
        String result = String.format("%s %s %s", headline, hashTag, followOnLink);
        if (result.length() > TWEET_LENGTH) {
            Integer diff = (result.length() + hashTag.length() + 1) - (TWEET_LENGTH + 4);
            String hlText = headline.substring(0, headline.length() - diff) + "... ";
            result = String.format("%s %s %s", hlText, hashTag, followOnLink);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Headline headline1 = (Headline) o;
        return Objects.equals(headline, headline1.headline);
    }

    @Override
    public int hashCode() {

        return Objects.hash(headline);
    }
}
