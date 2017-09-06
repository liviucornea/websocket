package com.websocket.controllers;

import com.websocket.data.Greeting;
import com.websocket.data.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {
    final Logger logger = LoggerFactory.getLogger(GreetingController.class);
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        logger.warn("Method greetings is invocked with message" + message.toString());
        Thread.sleep(100); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

}
