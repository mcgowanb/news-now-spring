package com.mcgowan.newsnow;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EventProcessor implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object o, Context context) {
        log.info("Loading clr from aws trigger");
        SpringApplication.run(NewsnowApplication.class);
        return "Ok";
    }
}
