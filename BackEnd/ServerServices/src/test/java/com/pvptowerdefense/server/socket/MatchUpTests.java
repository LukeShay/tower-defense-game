package com.pvptowerdefense.server.socket;

import com.pvptowerdefense.server.socket.models.MatchUp;
import com.pvptowerdefense.server.socket.models.SocketMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.websocket.Session;

/**
 * These tests will only test a few methods until mocking is in place for sessions.
 */
class MatchUpTests {
	@Mock Session sessionOne;
	@Mock Session sessionTwo;

	private MatchUp matchUp;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(sessionOne.isOpen()).thenReturn(true);
		Mockito.when(sessionTwo.isOpen()).thenReturn(true);

		matchUp = new MatchUp("test1", sessionOne, "test2", sessionTwo);
	}

	@Test
	void preGameMessageTest() {
		matchUp.sendPreGameMessage();
		SocketMessage socketMessage = matchUp.getSocketMessage();

		Assertions.assertAll(
				() -> Assertions.assertEquals("pre-game", socketMessage.getGameState(), "Game state is incorrect"),
				() -> Assertions.assertEquals("test1", socketMessage.getPlayerOneId(), "Player one id is incorrect"),
				() -> Assertions.assertEquals("test2", socketMessage.getPlayerTwoId(), "Player two id is incorrect"),
				() -> Assertions.assertEquals(0, socketMessage.getCurrentTime(), "Current time is incorrect"),
				() -> Assertions.assertEquals("", socketMessage.getWinner(), "Winner is incorrect"),
				() -> Assertions.assertEquals("select deck", socketMessage.getServerMessage(), "Server message is incorrect.")
		);
	}

	@Test
	void startingGameMessageTest() {
		matchUp.sendStartGameMessage();
		SocketMessage socketMessage = matchUp.getSocketMessage();

		Assertions.assertAll(
				() -> Assertions.assertEquals("starting-game", socketMessage.getGameState(), "Game state is incorrect"),
				() -> Assertions.assertEquals("test1", socketMessage.getPlayerOneId(), "Player one id is incorrect"),
				() -> Assertions.assertEquals("test2", socketMessage.getPlayerTwoId(), "Player two id is incorrect"),
				() -> Assertions.assertEquals(0, socketMessage.getCurrentTime(), "Current time is incorrect"),
				() -> Assertions.assertEquals("", socketMessage.getWinner(), "Winner is incorrect"),
				() -> Assertions.assertEquals("starting game in 3 seconds", socketMessage.getServerMessage(), "Server message is incorrect.")
		);
	}

	@Test
	void postGameMessageTest() {
		matchUp.sendPostGameMessage(0);
		SocketMessage socketMessage = matchUp.getSocketMessage();

		Assertions.assertAll(
				() -> Assertions.assertEquals("post-game", socketMessage.getGameState(), "Game state is incorrect"),
				() -> Assertions.assertEquals("test1", socketMessage.getPlayerOneId(), "Player one id is incorrect"),
				() -> Assertions.assertEquals("test2", socketMessage.getPlayerTwoId(), "Player two id is incorrect"),
				() -> Assertions.assertEquals(0, socketMessage.getCurrentTime(), "Current time is incorrect"),
				() -> Assertions.assertEquals("test2", socketMessage.getWinner(), "Winner is incorrect"),
				() -> Assertions.assertEquals("the game is over", socketMessage.getServerMessage(), "Server message is incorrect.")
		);
	}

	@Test
	void postGameMessageOneForfeitTest() {
		Mockito.when(sessionTwo.isOpen()).thenReturn(false);
		matchUp.sendPostGameMessage(0);
		SocketMessage socketMessage = matchUp.getSocketMessage();

		Assertions.assertAll(
				() -> Assertions.assertEquals("post-game", socketMessage.getGameState(), "Game state is incorrect"),
				() -> Assertions.assertEquals("test1", socketMessage.getPlayerOneId(), "Player one id is incorrect"),
				() -> Assertions.assertEquals("test2", socketMessage.getPlayerTwoId(), "Player two id is incorrect"),
				() -> Assertions.assertEquals(0, socketMessage.getCurrentTime(), "Current time is incorrect"),
				() -> Assertions.assertEquals("test1", socketMessage.getWinner(), "Winner is incorrect"),
				() -> Assertions.assertEquals("the game is over", socketMessage.getServerMessage(), "Server message is incorrect.")
		);
	}
}
