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

    @Override
    public String toString() {
        return String.format("%s %s", headline, followOnLink);
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
