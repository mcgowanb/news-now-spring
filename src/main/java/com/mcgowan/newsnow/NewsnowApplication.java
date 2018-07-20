package com.mcgowan.newsnow;

import com.mcgowan.newsnow.service.INewsNow;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;

@SpringBootApplication
public class NewsnowApplication implements CommandLineRunner {

    @Inject
    private INewsNow newsNow;

    public static void main(String[] args) {
        SpringApplication.run(NewsnowApplication.class, args);
    }

    @Override
    public void run(String... args) {
        newsNow.process();
    }
}

