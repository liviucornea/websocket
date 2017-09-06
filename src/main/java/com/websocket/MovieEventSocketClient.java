package com.websocket;

import com.websocket.controllers.FirstController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

// based on http://stackoverflow.com/questions/26452903/javax-websocket-client-simple-example

@ClientEndpoint
public class MovieEventSocketClient {
    final Logger logger = LoggerFactory.getLogger(MovieEventSocketClient.class);
    public MovieEventSocketClient(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Session userSession = null;

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("client: opening websocket ");
        logger.warn("Client oppening web socket");
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("client: closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        logger.warn("Client web socket recived message");
        System.out.println("client: received message "+message);
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

}