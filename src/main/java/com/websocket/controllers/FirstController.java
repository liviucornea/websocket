package com.websocket.controllers;

import com.websocket.MovieEventSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class FirstController extends BaseController {
    final Logger logger = LoggerFactory.getLogger(FirstController.class);
    private MovieEventSocketClient client;
   // private final String webSocketAddress = "ws://localhost:8080/websocket/gs-guide-websocket";
    private final String webSocketAddress = "ws://localhost:8080/websocket/cinemaSocket/0";
    private void initializeWebSocket() throws URISyntaxException {
//ws://localhost:7101/CinemaMonitor/cinemaSocket/
         client = new MovieEventSocketClient(new URI(webSocketAddress ));
        logger.warn("Client web scoket create and listen at at " + webSocketAddress);
    }

    private void sendMessageOverSocket(String message) {
        if (client == null) {
            try {
                initializeWebSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        client.sendMessage(message);

    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    @ResponseBody
    public String getData() {
        String res = "Hi Liviu!!! Application is working. I will send the message to screen pop as well!";
        sendMessageOverSocket("{\"name\":\"Hi Liviu Cornea - message from dispatch mediator!\"}");
        return res;
    }
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    @ResponseBody
    public String getDData() {
        logger.warn("First controller is invoked");
        String res = "Ceau UNKNOWN!!! Application is working ........Heloooooooo...";
        return res;
    }




}
