package com.mcgowan.newsnow.service;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface IWebPageLoader {
    Document getWebPage(String url) throws IOException;
}
