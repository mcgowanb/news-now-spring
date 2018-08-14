package com.mcgowan.newsnow;

import com.mcgowan.newsnow.service.INewsNow;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import twitter4j.TwitterException;

import javax.inject.Inject;
import java.io.IOException;

@SpringBootApplication
@Log4j2
public class NewsnowApplication implements CommandLineRunner {

    @Inject
    private INewsNow newsNow;

    public static void main(String[] args) {
        SpringApplication.run(NewsnowApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException, TwitterException {
        log.info("Running process method");
        newsNow.process();
    }
}

