package com.pvptowerdefense.server.socket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SocketTests {
	@LocalServerPort
	private static int port;

	@Test
	@Disabled
	void connectToSocketTest() throws InterruptedException, IOException {
		SS5WebSocketClient webSocket1 = new SS5WebSocketClient("1", port);
		SS5WebSocketClient webSocket2 = new SS5WebSocketClient("2", port);
		SS5WebSocketClient webSocket3 = new SS5WebSocketClient("3", port);
		SS5WebSocketClient webSocket4 = new SS5WebSocketClient("4", port);

		Assertions.assertAll(
				() -> Assertions.assertTrue(webSocket1.waitForConnection()),
				() -> Assertions.assertTrue(webSocket2.waitForConnection()),
				() -> Assertions.assertTrue(webSocket3.waitForConnection()),
				() -> Assertions.assertTrue(webSocket4.waitForConnection()));

		Assertions.assertAll(
				() -> Assertions.assertTrue(webSocket1.isOpen()),
				() -> Assertions.assertTrue(webSocket2.isOpen()),
				() -> Assertions.assertTrue(webSocket3.isOpen()),
				() -> Assertions.assertTrue(webSocket4.isOpen())
		);

		webSocket1.sendMessage("####### HELLO1 #######");
		webSocket2.sendMessage("####### HELLO2 #######");
		webSocket3.sendMessage("####### HELLO3 #######");
		webSocket4.sendMessage("####### HELLO4 #######");

		Thread.sleep(10000);

		System.out.println(webSocket1.getMessages().toString());
		System.out.println(webSocket2.getMessages().toString());
		System.out.println(webSocket3.getMessages().toString());
		System.out.println(webSocket4.getMessages().toString());

		webSocket1.close();
		webSocket2.close();
		webSocket3.close();
		webSocket4.close();

		Thread.sleep(5000);

		Assertions.assertAll(
				() -> Assertions.assertTrue(webSocket1.isClosed()),
				() -> Assertions.assertTrue(webSocket2.isClosed()),
				() -> Assertions.assertTrue(webSocket3.isClosed()),
				() -> Assertions.assertTrue(webSocket4.isClosed())
		);
	}
}