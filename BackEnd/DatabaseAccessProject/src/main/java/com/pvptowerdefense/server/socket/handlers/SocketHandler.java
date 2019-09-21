package com.pvptowerdefense.server.socket.handlers;

import com.pvptowerdefense.server.socket.models.MatchUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Handler for socket connections. The request has to be sent to the domain
 * url/socket/{id} where id is the phone id.
 */
@Component
@ServerEndpoint("/socket/{id}")
public class SocketHandler {
	private HashMap<String, Session> idAndSession;
	private HashMap<Session, String> sessionAndId;
	private List<MatchUp> matchUpList;

	private static Logger logger =
			LoggerFactory.getLogger(SocketHandler.class.getName());

	/**
	 * This class handles the incoming socket requests.
	 */
	public SocketHandler() {
		idAndSession = new HashMap<>();
		sessionAndId = new HashMap<>();
		matchUpList = new ArrayList<>();
	}

	/**
	 * When a connection is opened, the session and id is added to the hash
	 * maps.
	 *
	 * @param session The new session.
	 * @param id The id of the user as a String.
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("id") String id) {
		idAndSession.put(id, session);
		sessionAndId.put(session, id);

	}

	/**
	 * Sends the message to the user that the inputted session is matched up
	 * with.
	 *
	 * @param session The session sending the message.
	 * @param message The message as a String. *NOTE This might change types.
	 */
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		// Currently does nothing because we have not implemented matchmaking
		// MatchUp matchup = findMatchUp(session);
		// if (matchup != null) {
		// matchup.getOtherSession(session).getAsyncRemote().sendText(message);
		// }
		broadcast(message);
	}

	/**
	 * Removes the session from the hash maps and it's match up from the list.
	 *
	 * @param session The session that is leaving.
	 * @param id The id of the user as a String.
	 */
	@OnClose
	public void onClose(Session session, @PathParam("id") String id) {
		idAndSession.remove(id);
		sessionAndId.remove(session);

		matchUpList.remove(findMatchUp(session));
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.error("ERROR " + throwable.getMessage());
	}

	private void broadcast(String message) throws IOException {
		logger.info(message);
		sessionAndId.forEach((session, id) -> {
			session.getAsyncRemote().sendText(message);
		});
	}


	/**
	 * Finds the match up of the inputted session.
	 *
	 * @param session The session whose match up is being looked for.
	 * @return The match up.
	 */
	private MatchUp findMatchUp(Session session) {
		try {
			return matchUpList.stream().filter(getMatchUpPredicate(session))
					.collect(Collectors.toList()).get(0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Helper for findMatchUp.
	 *
	 * @param session The session being looked for.
	 * @return Predicate for the match up.
	 */
	private Predicate<MatchUp> getMatchUpPredicate(Session session) {
		return matchUp -> matchUp.getPlayerOneSession().equals(session) ||
				matchUp.getPlayerTwoSession().equals(session);
	}
}