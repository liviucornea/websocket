package com.websocket;
/*
https://technology.amis.nl/2015/05/14/java-web-application-sending-json-messages-through-websocket-to-html5-browser-application-for-real-time-push/

 */


import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/cinemaSocket/{client-id}")
public class CinemaEventSocketMediator {

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet());
    final Logger logger = LoggerFactory.getLogger(CinemaEventSocketMediator.class);
    @OnMessage
    public String onMessage(String message, Session session, @PathParam("client-id") String clientId) {
        try {
            logger.warn("Mediator is receiveing message:" + message);
            JSONObject jObj = new JSONObject(message);
            logger.warn("Message is converted to JSON" );
            System.out.println("received message from client " + clientId);

            for (Session s : peers) {
                try {
                    s.getBasicRemote().sendText(message);
                    logger.warn("Mediator is sending recived message to peer");
                    System.out.println("send message to peer ");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "message was received by socket mediator and processed: " + message;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("client-id") String clientId) {
        System.out.println("mediator: opened websocket channel for client " + clientId);
        peers.add(session);

        try {
            session.getBasicRemote().sendText("good to be in touch");
        } catch (IOException e) {
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("client-id") String clientId) {
        System.out.println("mediator: closed websocket channel for client " + clientId);
        peers.remove(session);
    }
}