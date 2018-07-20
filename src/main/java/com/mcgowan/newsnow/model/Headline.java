package com.mcgowan.newsnow.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Headline {

    private String headline;
    private String link;
    private String followOnLink;

}
