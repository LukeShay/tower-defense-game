package com.pvptowerdefense.test.websocketclient;

import com.pvptowerdefense.test.queue.Queue;
<<<<<<< HEAD
=======
import org.java_websocket.handshake.ServerHandshake;
>>>>>>> bc9b4c708a8d012f417d1108984cf874ba2ba531
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
<<<<<<< HEAD
import java.io.IOException;
=======
>>>>>>> bc9b4c708a8d012f417d1108984cf874ba2ba531
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The type My web socket client.
 */
@ClientEndpoint
public class SS5WebSocketClient {
<<<<<<< HEAD
	/**
	 * This is the server url. For testing locally the url is
	 * 'http://localhost:8080/socket/%s'. For testing on the server the url
	 * is 'ws://coms-309-ss-5.misc.iastate.edu:8080/socket/%s'.
	 */
	private static final String serverUrl = "ws://localhost:8080/socket/%s";
=======
	private static final String serverUrl = "http://localhost:8080/socket/%s";
>>>>>>> bc9b4c708a8d012f417d1108984cf874ba2ba531

	private String id = null;
	private Queue<String> messages = null;
	private Session session = null;

	private static Logger logger =
			LoggerFactory.getLogger(SS5WebSocketClient.class);

	private Session userSession = null;

	/**
	 * Instantiates a new My web socket client.
	 *
	 * @param userId the socket id
	 * @throws URISyntaxException the uri syntax exception
	 */
	public SS5WebSocketClient(String userId) throws URISyntaxException {
<<<<<<< HEAD
		this.id = userId;
=======
		this.socketId = userId;
>>>>>>> bc9b4c708a8d012f417d1108984cf874ba2ba531
		messages = new Queue<>();

		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, new URI(String.format(serverUrl,
					userId)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
<<<<<<< HEAD
	}

	@OnOpen
	public void onOpen(Session session) {
		messages.enqueue(String.format("Connected: %s%s", serverUrl, id));
		setSession(session);
	}

	@OnMessage
	public void onMessage(String message) {
		logger.info("Message " + id);
		messages.enqueue(String.format("Message: %s", message));
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		messages.enqueue(String.format("Closed: %s", reason.getReasonPhrase()));
		this.session = null;
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		messages.enqueue(String.format("Error: %s", throwable.getMessage()));
	}

	/**
	 * Sends message to server.
	 *
	 * @param message the message
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * Closes session.
	 *
	 * @throws IOException the io exception
	 */
	public void close() throws IOException {
		session.close();
		session = null;
	}

	/**
	 * Is open boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOpen() {
		return session.isOpen();
	}

	/**
	 * Is closed boolean.
	 *
	 * @return the boolean
	 */
	public boolean isClosed() {
		return session == null || !session.isOpen();
	}

	/**
	 * Gets messages.
	 *
	 * @return the messages
	 */
=======
	}

	@OnOpen
	public void onOpen(Session session) {
		messages.enqueue(String.format("Connected to %s%s", serverUrl, socketId));
		setUserSession(session);
	}

	@OnMessage
	public void onMessage(String message) {
		messages.enqueue(String.format("Message: %s", message));
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		messages.enqueue(String.format("Closed: %s", reason.getReasonPhrase()));
		this.userSession = null;
	}

	@OnError
	public void onError(Exception e) {
		messages.enqueue("Error");
	}

	public void sendMessage(String message) {
		this.userSession.getAsyncRemote().sendText(message);
	}

>>>>>>> bc9b4c708a8d012f417d1108984cf874ba2ba531
	public Queue<String> getMessages() {
		return messages;
	}

<<<<<<< HEAD
	/**
	 * Gets server url.
	 *
	 * @return the server url
	 */
=======
>>>>>>> bc9b4c708a8d012f417d1108984cf874ba2ba531
	public static String getServerUrl() {
		return serverUrl;
	}

<<<<<<< HEAD
	/**
	 * Gets socket id.
	 *
	 * @return the socket id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets socket id.
	 *
	 * @param id the socket id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets messages.
	 *
	 * @param messages the messages
	 */
=======
	public String getSocketId() {
		return socketId;
	}

	public void setSocketId(String socketId) {
		this.socketId = socketId;
	}

>>>>>>> bc9b4c708a8d012f417d1108984cf874ba2ba531
	public void setMessages(Queue<String> messages) {
		this.messages = messages;
	}

<<<<<<< HEAD
	/**
	 * Gets user session.
	 *
	 * @return the user session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Sets user session.
	 *
	 * @param session the user session
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Wait for connection boolean.
	 *
	 * @return the boolean
	 */
	public boolean waitForConnection() {
		for (int i = 0; i < 12000 && !isOpen(); i++) {
			nap();
		}

		return isOpen();
=======
	public Session getUserSession() {
		return userSession;
	}

	public void setUserSession(Session userSession) {
		this.userSession = userSession;
>>>>>>> bc9b4c708a8d012f417d1108984cf874ba2ba531
	}

	private void nap() {
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
